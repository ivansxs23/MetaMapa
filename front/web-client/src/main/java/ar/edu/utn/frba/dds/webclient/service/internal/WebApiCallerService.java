package ar.edu.utn.frba.dds.webclient.service.internal;

import ar.edu.utn.frba.dds.webclient.dto.input.Auth.AuthResponseDTO;
import ar.edu.utn.frba.dds.webclient.dto.RefreshTokenDTO;
import ar.edu.utn.frba.dds.webclient.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Servicio genérico para hacer llamadas HTTP con manejo automático de tokens
 */
@Service
public class WebApiCallerService {

    private final WebClient webClient;
    private final String authServiceUrl;

    public WebApiCallerService(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.authServiceUrl = authServiceUrl;
    }

    /**
     * Ejecuta una llamada al API con manejo automático de refresh token
     * @param apiCall función que ejecuta la llamada al API
     * @return resultado de la llamada al API
     */
    public <T> T executeWithTokenRetry(ApiCall<T> apiCall) {
        String accessToken = getAccessTokenFromSession();
        System.out.println("Access Token en WebApiCallerService: " + accessToken);
        String refreshToken = getRefreshTokenFromSession();

        if (accessToken == null) {
            return apiCall.execute(null);
        }

        try {
            // Primer intento con el token actual
            return apiCall.execute(accessToken);
        } catch (WebClientResponseException e) {
            if ((e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) && refreshToken != null) {
                try {
                    // Token expirado, intentar refresh
                    AuthResponseDTO newTokens = refreshToken(refreshToken);

                    // Segundo intento con el nuevo token
                    return apiCall.execute(newTokens.getAccessToken());
                } catch (Exception refreshError) {
                    throw new RuntimeException("Error al refrescar token y reintentar: " + refreshError.getMessage(), refreshError);
                }
            }
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new NotFoundException("Recurso no encontrado");
            }
            throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta una llamada HTTP GET
     */
    public <T> T get(String url, Class<T> responseType) {
        return executeWithTokenRetry(accessToken ->
            webClient.get()
                .uri(url)
                .headers(headers -> {
                    if (accessToken != null) {
                        headers.setBearerAuth(accessToken);
                    }
                })
                .retrieve()
                .bodyToMono(responseType)
                .block()
        );
    }

    /**
     * Ejecuta una llamada HTTP GET que retorna una lista
     */
    public <T> java.util.List<T> getList(String url, Class<T> responseType) {
        return executeWithTokenRetry(accessToken ->
                webClient
                        .get()
                        .uri(url)
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .bodyToFlux(responseType)
                        .collectList()
                        .block()
        );
    }

    /**
     * Ejecuta una llamada HTTP GET con un token específico (sin usar sesión)
     */
    public <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
        try {
            return webClient
                    .get()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta una llamada HTTP POST
     */
    public <T> T post(String url, Object body, Class<T> responseType) {
        System.out.println("POST a SolicitudDeElminacion");
        return executeWithTokenRetry(accessToken ->
                webClient
                        .post()
                        .uri(url)
                    .headers(headers -> {
                        if (accessToken != null) {
                            headers.setBearerAuth(accessToken);
                        }
                    })
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
                        .block()
        );
    }

    /**
     * Ejecuta una llamada HTTP PUT
     */
    public <T> T put(String url, Object body, Class<T> responseType) {
        return executeWithTokenRetry(accessToken ->
                webClient
                        .put()
                        .uri(url)
                        .header("Authorization", "Bearer " + accessToken)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
                        .block()
        );
    }

    /**
     * Ejecuta una llamada HTTP DELETE
     */
    public void delete(String url) {
        executeWithTokenRetry(accessToken -> {
            webClient
                    .delete()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return null;
        });
    }

    /**
     * Refresca el access token usando el refresh token
     */
    private AuthResponseDTO refreshToken(String refreshToken) {
        try {
            RefreshTokenDTO refreshRequest = RefreshTokenDTO.builder()
                    .refreshToken(refreshToken)
                    .build();

            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/api/auth/refresh")
                    .bodyValue(refreshRequest)
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();

            // Actualizar tokens en sesión
            assert response != null;
            updateTokensInSession(response.getAccessToken(), response.getRefreshToken());
            return response;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Refresh falló. status=" + e.getStatusCode() +
                " body=" + e.getResponseBodyAsString(),
                e);
        }
    }

    /**
     * Obtiene el access token de la sesión
     */
    private String getAccessTokenFromSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getSession().getAttribute("accessToken");
    }

    /**
     * Obtiene el refresh token de la sesión
     */
    private String getRefreshTokenFromSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getSession().getAttribute("refreshToken");
    }

    /**
     * Actualiza los tokens en la sesión
     */
    private void updateTokensInSession(String accessToken, String refreshToken) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        request.getSession().setAttribute("accessToken", accessToken);
        request.getSession().setAttribute("refreshToken", refreshToken);
    }

    /**
     * Interfaz funcional para ejecutar llamadas al API con token
     */
    @FunctionalInterface
    public interface ApiCall<T> {
        T execute(String accessToken);
    }
}