package ar.edu.utn.frba.dds.clienteUtils;


import ar.edu.utn.frba.dds.entities.dto.input.EstadisticasInputDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ClienteEstadistica {
    private final WebClient webClient;
    public ClienteEstadistica(@Value("${fuente.agregador.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }
    public Mono<List<EstadisticasInputDTO>> obtenerEstadisticas() {
        return webClient.get()
                .uri("/estadisticas")
                .retrieve()
                .bodyToFlux(EstadisticasInputDTO.class)
                .doOnNext(dto -> System.out.println("Datos recibidos: " + dto))
                .collectList();
    }
}
