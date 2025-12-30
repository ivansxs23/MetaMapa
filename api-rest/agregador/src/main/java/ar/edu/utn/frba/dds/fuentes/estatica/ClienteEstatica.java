package ar.edu.utn.frba.dds.fuentes.estatica;

import ar.edu.utn.frba.dds.entities.dto.input.HechoEstaticoDto;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import ar.edu.utn.frba.dds.entities.dto.input.PageResponse;
import ar.edu.utn.frba.dds.fuentes.dinamica.ClienteDinamica;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ClienteEstatica {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ClienteEstatica.class);
    public ClienteEstatica(@Value("${fuente.estatica.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }
    public Mono<List<HechoEstaticoDto>> buscarHechos() {
        return webClient.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoEstaticoDto.class)
                .collectList();
    }
    public MetadataDTO buscarMetadata() {
        return webClient.get()
            .uri("/metadata")
            .retrieve()
            .bodyToMono(MetadataDTO.class)
            .block();
    }
    public PageResponse<HechoEstaticoDto> buscarHechosPaginable(Integer page, Integer size, LocalDateTime fecha) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/hechos")
                    .queryParam("fechaModificacionDesde", fecha)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoEstaticoDto>>() {
                })
                .block();
        }catch (Exception ex) {
            logger.warn("Estatica no disponible, se omite importación", ex);
            return new PageResponse<>(); // ✅ página vacía
        }

    }

}
