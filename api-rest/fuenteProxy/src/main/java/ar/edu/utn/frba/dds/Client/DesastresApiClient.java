package ar.edu.utn.frba.dds.Client;

import ar.edu.utn.frba.dds.dto.input.LoginResponseDTO.LoginResponse;
import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.PaginatedResponseDTO;
import ar.edu.utn.frba.dds.dto.output.LoginRequestDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
@Component
public class DesastresApiClient {

  private final WebClient webClient;
  private final String mail;
  private final String contrasenia;
  private String apiKey;
  private String tipoToken;
  @Getter
  private final String baseUrl;

  public DesastresApiClient(
      @Value("${desastres-naturales.api.mail}") String mail,
      @Value("${desastres-naturales.api.password}") String password,
      @Value("${desastres-naturales.api.base-url}") String baseUrl
  ) {

    this.mail = mail;
    this.baseUrl = baseUrl;
    this.contrasenia = password;
    this.webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build();

  }

  private Mono<Void> iniciarSesion() {
    return webClient.post()
        .uri("/api/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new LoginRequestDTO(this.mail, this.contrasenia))
        .retrieve()
        .bodyToMono(LoginResponse.class)
        .doOnNext(response -> {
          this.apiKey = response.getData().getAccess_token();
          this.tipoToken = response.getData().getToken_type();
        })
        .then();
  }
  public Mono<PaginatedResponseDTO> obtenerPagina(String url) {
    return webClient.get()
        .uri(url)
        .headers(h -> h.set(HttpHeaders.AUTHORIZATION, tipoToken + " " + apiKey))
        .retrieve()
        .bodyToMono(PaginatedResponseDTO.class)
        .onErrorResume(WebClientResponseException.Unauthorized.class, ex ->
            iniciarSesion().then(obtenerPagina(url))
        );
  }
}
