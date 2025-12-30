package ar.edu.utn.frba.dds.fuentes.dinamica;

import ar.edu.utn.frba.dds.entities.dto.input.HechoDinamicoDto;
import ar.edu.utn.frba.dds.entities.dto.input.HechoEstaticoDto;
import ar.edu.utn.frba.dds.entities.dto.input.NuevoHechoDTO;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import ar.edu.utn.frba.dds.entities.dto.input.PageResponse;
import ar.edu.utn.frba.dds.entities.dto.output.NuevoHechoDinamicaDTO;
import ar.edu.utn.frba.dds.services.impl.HechoService;
import ar.edu.utn.frba.dds.utils.HechoMapper;
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
public class ClienteDinamica {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ClienteDinamica.class);

    public ClienteDinamica(@Value("${fuente.dinamica.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }
    public Mono<List<HechoDinamicoDto>> buscarHechos() {
        return webClient.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoDinamicoDto.class)
                .collectList();
    }
    public MetadataDTO buscarMetadata() {
        return webClient.get()
                .uri("/metadata")
                .retrieve()
                .bodyToMono(MetadataDTO.class)
                .block();
    }
    public Mono<HechoDinamicoDto> crearHecho(NuevoHechoDinamicaDTO hecho){
        return webClient.post()
                .uri("/hechos")
                .bodyValue(hecho)
                .retrieve()
                .bodyToMono(HechoDinamicoDto.class);
    }
    public Mono<HechoDinamicoDto> editarHecho(Long id, NuevoHechoDinamicaDTO hecho){
        return webClient.put()
                .uri("/hechos/{id}", id)
                .bodyValue(hecho)
                .retrieve()
                .bodyToMono(HechoDinamicoDto.class);
    }
    public PageResponse<HechoDinamicoDto> buscarHechosPaginable(Integer page, Integer size, LocalDateTime fecha) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/hechos")
                    .queryParam("ultimaEdicionDesde", fecha)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDinamicoDto>>() {
                })
                .block();
        }catch (Exception ex) {
            logger.warn("Dinámica no disponible, se omite importación", ex);
            return new PageResponse<>(); // ✅ página vacía
        }


    }
}
