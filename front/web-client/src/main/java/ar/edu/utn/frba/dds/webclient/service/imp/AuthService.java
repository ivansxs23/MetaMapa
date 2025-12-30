package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.LoginRequestDTO;
import ar.edu.utn.frba.dds.webclient.dto.RolesPermisosDTO;
import ar.edu.utn.frba.dds.webclient.dto.UserDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.Auth.AuthResponseDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.Auth.SignupResponseDTO;
import ar.edu.utn.frba.dds.webclient.service.IAuthService;
import ar.edu.utn.frba.dds.webclient.service.internal.WebApiCallerService;
import ar.edu.utn.frba.dds.webclient.utils.ExceptionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class AuthService implements IAuthService {

  private final WebClient webClient;
  private final String authServiceUrl;
  private final WebApiCallerService webApiCallerService;
  private final ExceptionFactory exceptionFactory;

  public AuthService(@Value("${auth.service.url}") String authServiceUrl, WebApiCallerService webApiCallerService, ExceptionFactory exceptionFactory) {
    this.webApiCallerService = webApiCallerService;
    this.exceptionFactory = exceptionFactory;
    this.webClient = WebClient.builder().build();
    this.authServiceUrl = authServiceUrl;
  }

  @Override
  public AuthResponseDTO login(LoginRequestDTO requestDTO) {
    try {
      return webClient
          .post()
          .uri(authServiceUrl + "/api/auth/login")
          .bodyValue(requestDTO)
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)
          .block();
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return null;
      }
      // Otros errores HTTP
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }


  @Override
  public void signUp(UserDTO requestDTO) {
    try {
    webClient
        .post()
        .uri(authServiceUrl + "/api/auth/register")
        .bodyValue(requestDTO)
        .retrieve()
        .bodyToMono(SignupResponseDTO.class)
        .block();
  } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.CONFLICT) {
        System.out.println("Error de duplicado detectado: ");
        throw exceptionFactory.construirExcepcionDuplicado(e);
      }
      if (e.getStatusCode() != HttpStatus.valueOf(200)) {
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
      }
  } catch (Exception e) {
    throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
  }
  }

  @Override
  public RolesPermisosDTO getRolesPermisos(String accessToken) {
    try {
      return webApiCallerService.getWithAuth(
          authServiceUrl + "/api/auth/user/roles-permisos",
          accessToken,
          RolesPermisosDTO.class
      );
    } catch (Exception e) {
      throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
    }
  }
}
