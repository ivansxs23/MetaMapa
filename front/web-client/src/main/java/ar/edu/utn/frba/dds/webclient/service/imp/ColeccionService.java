package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionDetalladoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.output.ColeccionDTO;
import ar.edu.utn.frba.dds.webclient.service.IColeccionService;
import ar.edu.utn.frba.dds.webclient.exceptions.NotFoundException;
import java.util.Optional;

import ar.edu.utn.frba.dds.webclient.service.internal.WebApiCallerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
public class ColeccionService implements IColeccionService {

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;

    public ColeccionService(@Value("${api.backend.url}") String baseUrl, WebApiCallerService webApiCallerService) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.webApiCallerService = webApiCallerService;
    }

    // ================================
    // 🔹 Obtener todas las colecciones
    // ================================
    @Override
    public List<ColeccionInputDTO> obtenerTodasColecciones() {
        try {
            return webClient.get()
                    .uri("/api/colecciones")
                    .retrieve()
                    .bodyToFlux(ColeccionInputDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("No se pudieron obtener las colecciones");
        }
    }

    // ================================
    // 🔹 Obtener colección por ID
    // ================================
    public ColeccionDetalladoInputDTO obtenerColeccionPorId(Long id) {
        ColeccionDetalladoInputDTO coleccion = webClient.get()
                .uri("api/colecciones/{id}", id)
                .retrieve()
                .bodyToMono(ColeccionDetalladoInputDTO.class)
                .block();
        if (coleccion == null) {
            throw new NotFoundException("Colección no encontrada con id: " + id);
        }
        return coleccion;
    }

    // ================================
    // 🔹 Crear colección
    public String crearColeccion(ColeccionDTO dto) {
        try {
            return webClient.post()
                    .uri("/api/colecciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al crear la colección: " + e.getMessage());
        }
    }





    // ================================
    // 🔹 Editar colección existente
    // ================================
    public void editarColeccion(Long id, ColeccionDTO dto) {
        try {
            log.error("DTO ENVIADO AL BACKEND: {}", dto);
            webClient.patch()
                    .uri("api/colecciones/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(Void.class) // 👈 clave
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new NotFoundException("Colección no encontrada para editar: " + id);
        } catch (WebClientResponseException e) {
            log.error("Error al editar colección: {}", e.getMessage());
            throw new RuntimeException("Error al editar la colección");
        }
    }



    // ================================
    // 🔹 Eliminar colección
    // ================================
    public void eliminarColeccion(Long id) {
        try {
            webClient.delete()
                    .uri("api/colecciones/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new NotFoundException("Colección no encontrada para eliminar: " + id);
        } catch (WebClientResponseException e) {
            log.error("Error al eliminar colección: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la colección");
        }
    }

    // ================================
    // 🔹 Obtener hechos de una colección
    // ================================
    public RestPageResponse<HechoInputDTO> obtenerHechosDeColeccion(Long id,int page, int size, FiltroDTO filtro) {
        try {
            return webClient.get()
                .uri(uriBuilder -> {
                    var ub = uriBuilder
                        .pathSegment("api", "colecciones", String.valueOf(id), "hechos")
                        .queryParam("activo", true)
                        .queryParamIfPresent("idCategoria", Optional.ofNullable(filtro.getIdCategoria()))
                        .queryParamIfPresent("provincia", Optional.ofNullable(filtro.getProvincia()))
                        .queryParamIfPresent("fuente", Optional.ofNullable(filtro.getFuente()))
                        .queryParamIfPresent("fechaAcontecimientoDesde",
                            Optional.ofNullable(filtro.getFechaAcontecimientoDesde()))
                        .queryParamIfPresent("fechaAcontecimientoHasta",
                            Optional.ofNullable(filtro.getFechaAcontecimientoHasta()))

                        .queryParamIfPresent("curado", Optional.ofNullable(filtro.getCurado()))
                        .queryParamIfPresent("page",Optional.of(page))
                        .queryParamIfPresent("size", Optional.of(size));
                    if (Boolean.TRUE.equals(filtro.getEsAnonimo())) {
                        ub.queryParam("esAnonimo", false);
                    }

                    return ub.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPageResponse<HechoInputDTO>>() {})
                .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new NotFoundException("Colección no encontrada: " + id);
        } catch (WebClientResponseException e) {
            log.error("Error al obtener hechos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener hechos de la colección");
        }
    }


    // ================================
    // 🔹 Actualizar todas las colecciones
    // ================================
    public void actualizarTodasColecciones() {
        try {
            webClient.put()
                    .uri("api/colecciones/actualizar")
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error al actualizar colecciones: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar colecciones");
        }
    }

}

