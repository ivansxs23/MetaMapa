package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.CategoriaDTO;
import ar.edu.utn.frba.dds.webclient.service.ICategoriaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CategoriaService implements ICategoriaService {
  private final WebClient webClient;

  public CategoriaService(@Value("${api.backend.url}") String baseUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
  }

  @Override
  public List<CategoriaDTO> obtenerTodas() {
    try {
      return webClient.get()
          .uri("api/categorias")
          .retrieve()
          .bodyToFlux(CategoriaDTO.class)
          .collectList()
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("No se pudieron obtener las categorias");
    }
  }
}
