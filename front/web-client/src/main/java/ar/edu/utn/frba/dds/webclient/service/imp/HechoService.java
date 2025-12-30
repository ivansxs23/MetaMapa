package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.TipoMedia;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.service.IHechoService;
import ar.edu.utn.frba.dds.webclient.service.IMediaStorageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ar.edu.utn.frba.dds.webclient.service.internal.WebApiCallerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class HechoService implements IHechoService {

    private final WebClient agregadorWebClient;
    private final WebClient dinamicaWebClient;
    private final WebClient estaticaWebClient;
    private final WebApiCallerService webApiCallerService;
    private final String dinamicaUrl;
    private final String agregadorBaseUrl;
    private final IMediaStorageService mediaStorageService;

    public HechoService(@Value("${api.dinamica.url}") String dinamicaBaseUrl, @Value("${api.backend.url}") String agregadorBaseUrl, WebApiCallerService webApiCallerService, @Value("${api.estatica.url}") String estaticaBaseUrl, IMediaStorageService mediaStorageService) {
        this.agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
        this.dinamicaWebClient = WebClient.builder().baseUrl(dinamicaBaseUrl).build();
        this.estaticaWebClient = WebClient.builder().baseUrl(estaticaBaseUrl).build();
        this.webApiCallerService = webApiCallerService;
        this.dinamicaUrl = dinamicaBaseUrl;
        this.agregadorBaseUrl = agregadorBaseUrl;
      this.mediaStorageService = mediaStorageService;
    }

    public void crearHechoSinToken(HechoDTO hechoDTO) {
        try {
            log.info("Enviando nuevo hecho: {}", hechoDTO.getTitulo());
            dinamicaWebClient
                    .post()
                    .uri("/api/hechos")
                    .bodyValue(hechoDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            log.error("Error al crear el hecho: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el hecho");
        }
    }

    public RestPageResponse<HechoInputDTO> obtenerHechos(FiltroDTO filtro, int page, int size) {
        try {
            return agregadorWebClient
                    .get()
                    .uri(uriBuilder -> {
                        var ub = uriBuilder
                                .path("api/hechos")
                                    .queryParam("activo", true)
                                    .queryParamIfPresent("idCategoria", Optional.ofNullable(filtro.getIdCategoria()))
                                    .queryParamIfPresent("provincia", Optional.ofNullable(filtro.getProvincia()))
                                    .queryParamIfPresent("fuente", Optional.ofNullable(filtro.getFuente()))
                                    .queryParamIfPresent("fechaAcontecimientoDesde", Optional.ofNullable(filtro.getFechaAcontecimientoDesde()))
                                    .queryParamIfPresent("fechaAcontecimientoHasta",
                                        Optional.ofNullable(filtro.getFechaAcontecimientoHasta()))
                                    .queryParamIfPresent("curado", Optional.ofNullable(filtro.getCurado()))
                                    .queryParamIfPresent("page", Optional.of(page))
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
        } catch (WebClientResponseException e) {
            log.error("Error al obtener los hechos: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los hechos");
        }
    }

    @Override // Agrega esto a tu interfaz IHechoService
    public void importarHechosCsv(MultipartFile archivo) {
        try {
            log.info("Iniciando subida de CSV: {}", archivo.getOriginalFilename());

            // 1. Preparamos el cuerpo del archivo AQUÍ (Lógica de negocio/dominio)
            ByteArrayResource fileResource = new ByteArrayResource(archivo.getBytes()) {
                @Override
                public String getFilename() {
                    // Es vital sobreescribir esto o el backend recibirá un archivo sin nombre
                    return archivo.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // "archivo" es el nombre del @RequestParam en el backend
            body.add("archivo", fileResource);

            estaticaWebClient.post()
                .uri("/api/hechos/importar")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .toBodilessEntity()
                .subscribe();

        } catch (Exception e) {
            log.error("Error al importar CSV: {}", e.getMessage());
            throw new RuntimeException("Error al subir el archivo al servicio dinámico", e);
        }
    }

    public void crearHecho(HechoDTO hechoDTO, List<MultipartFile> files) {
        List<ArchivoOutputDTO> archivos = new ArrayList<>();

        for (MultipartFile f : files) {
            if (!f.isEmpty()) {
                String url = mediaStorageService.save(f);
                TipoMedia tipo = Objects.requireNonNull(f.getContentType()).startsWith("image")
                    ? TipoMedia.IMAGEN
                    : TipoMedia.VIDEO;
                ArchivoOutputDTO archivoOutputDTO = new ArchivoOutputDTO();
                archivoOutputDTO.setUrl(url);
                archivoOutputDTO.setTipo(tipo);
                archivos.add(archivoOutputDTO);
            }

        }
        hechoDTO.getArchivos().addAll(archivos);
        try {
            String accessToken;
            var attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            accessToken = (String) request.getSession().getAttribute("accessToken");

            if (accessToken != null) {
                webApiCallerService.post(dinamicaUrl + "/api/hechos", hechoDTO, String.class);
            } else {
                crearHechoSinToken(hechoDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el hecho", e);
        }
    }

    public HechoInputDTO obtenerHechoPorId(Long id) {
        try {
          return webApiCallerService.get(agregadorBaseUrl + "/api/hechos/" + id, HechoInputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public void editarHecho(Long id, HechoDTO hechoDTO) {
        try {
            webApiCallerService.put(dinamicaUrl + "/api/hechos/" + id, hechoDTO, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}