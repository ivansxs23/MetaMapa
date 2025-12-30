package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.services.impl.IpBlockingService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    // 1. Inyectamos el servicio de bloqueo
    private final IpBlockingService blockingService;

    // Cache de Buckets (Fichas)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // 2. Cache de "Abusos" (Para contar cuántas veces recibió error 429)
    private final Map<String, Integer> abuseTracker = new ConcurrentHashMap<>();

    // Constructor para conectar el servicio
    public RateLimitingFilter(IpBlockingService blockingService) {
        this.blockingService = blockingService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, this::createNewBucket);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // ÉXITO: Pasamos la petición
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));

            // Opcional: Si se porta bien, podríamos bajarle el contador de abusos
            // abuseTracker.remove(ip);

            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // FALLO (Error 429 - Too Many Requests)

            // --- LÓGICA DE BLOQUEO AUTOMÁTICO ---
            // Sumamos 1 al contador de abusos de esta IP
            int abuses = abuseTracker.merge(ip, 1, Integer::sum);

            System.out.println(">>> ALERTA: La IP " + ip + " ha excedido el límite " + abuses + " veces.");

            // 3. Si abusa más de 5 veces seguidas... ¡A LA CÁRCEL!
            if (abuses > 5) {
                blockingService.blockIp(ip);
                // (La próxima vez que intente entrar, lo frenará el IpAccessFilter con error 403)
            }
            // ------------------------------------

            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Demasiadas solicitudes. Intenta de nuevo en " + waitForRefill + " segundos.");
        }
    }

    private Bucket createNewBucket(String key) {
        return Bucket.builder()
                .addLimit(limit -> limit
                    .capacity(20)
                    .refillGreedy(20, Duration.ofMinutes(1))
                )
                .build();
    }
}