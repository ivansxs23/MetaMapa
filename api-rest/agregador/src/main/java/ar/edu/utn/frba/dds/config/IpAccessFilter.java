package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.services.impl.IpBlockingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1) // Se ejecuta PRIMERO
public class IpAccessFilter implements Filter {

    private final IpBlockingService blockingService;

    // Inyectamos valores del properties
    @Value("${security.ips.blocked:}")
    private String blockedIpsString;

    @Value("${security.ips.allowed:}")
    private String allowedIpsString;

    // Constructor para inyectar el servicio
    public IpAccessFilter(IpBlockingService blockingService) {
        this.blockingService = blockingService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String clientIp = request.getRemoteAddr();

        // DEBUG: Descomenta esto si necesitas ver tu IP en la consola
        // System.out.println(">>> IP Detectada: " + clientIp);

        // 0. EXCEPCIÓN ADMIN: Si vas a /api/admin, saltamos los chequeos
        // (Esto es vital para llamar al endpoint de desbloqueo si te baneas)
        if (request.getRequestURI().startsWith("/api/admin")) {
            chain.doFilter(req, res);
            return;
        }

        // 1. REVISIÓN AUTOMÁTICA
        if (blockingService.isBlocked(clientIp)) {
            response.setStatus(403);
            response.getWriter().write("ACCESO DENEGADO (Automático): Tu IP fue bloqueada por comportamiento sospechoso.");
            return;
        }

        // 2. REVISIÓN MANUAL (Lista Negra en properties)
        List<String> blockedList = Arrays.asList(blockedIpsString.split("\\s*,\\s*"));

        if (!blockedIpsString.isEmpty() && blockedList.contains(clientIp)) {
            response.setStatus(403);
            response.getWriter().write("ACCESO DENEGADO (Manual): Tu IP está en la lista negra del archivo de configuración.");
            return;
        }

        // 3. REVISIÓN MANUAL (Lista Blanca en properties)
       /* List<String> allowedList = Arrays.asList(allowedIpsString.split("\\s*,\\s*"));

        if (!allowedIpsString.isEmpty() && !allowedList.contains(clientIp)) {
            response.setStatus(403);
            response.getWriter().write("ACCESO DENEGADO: No estás en la lista de autorizados.");
            return;
        }
        */
        // Si pasó todos los filtros, adelante
        chain.doFilter(req, res);
    }
}