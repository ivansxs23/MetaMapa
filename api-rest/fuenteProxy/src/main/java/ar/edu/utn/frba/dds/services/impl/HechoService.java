package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.Client.DesastresApiClient;
import ar.edu.utn.frba.dds.dto.input.FiltroDTO;
import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.HechoDTO;
import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.PaginatedResponseDTO;
import ar.edu.utn.frba.dds.dto.output.PaginatedDTO;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.services.IHechoService;
import ar.edu.utn.frba.dds.utils.HechoMapper;
import ar.edu.utn.frba.dds.utils.IWebClient;
import ar.edu.utn.frba.dds.utils.RespuestaFactory;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class HechoService implements IHechoService {

  private final DesastresApiClient desastresApiClient;
  private final IWebClient webClient;
  private final HechoMapper hechoMapper;
  private final RespuestaFactory respuestaFactory;


  public HechoService(DesastresApiClient desastresApiClient, IWebClient webClient, HechoMapper hechoMapper, RespuestaFactory respuestaFactory) {
    this.desastresApiClient = desastresApiClient;
    this.webClient = webClient;
    this.hechoMapper = hechoMapper;
    this.respuestaFactory = respuestaFactory;
  }

  public Mono<PaginatedResponseDTO> obtenerPagina(Integer numeroPagina, Integer tamanioPagina) {
    String url = "/api/desastres?page=" + numeroPagina + "&per_page=" + tamanioPagina;
    return desastresApiClient.obtenerPagina(url);
  }
  @Override
  public PaginatedDTO obtenerHechos(FiltroDTO filtro, Pageable pageable) {

    int externalPage = pageable.getPageNumber() + 1;

    PaginatedResponseDTO pagina = this.obtenerPagina(externalPage, pageable.getPageSize()).block();
    if (pagina == null || pagina.getData() == null) {
      return respuestaFactory.armarRespuesta(pagina, List.of(), pageable);
    }

    Instant desde = (filtro != null && filtro.getFechaModificacionDesde() != null)
        ? filtro.getFechaModificacionDesde().atZone(ZoneOffset.UTC).toInstant()
        : null;

    List<HechoDTO> filtrados = pagina.getData().stream()
        .filter(Objects::nonNull)
        .filter(h -> h.getUpdated_at() != null && !h.getUpdated_at().isBlank())
        .filter(h -> {
          if (desde == null) return true;
          try {
            Instant updated = Instant.parse(h.getUpdated_at()); // "2025-05-06T22:14:14.000000Z"
            return !updated.isBefore(desde); // updated >= desde
          } catch (Exception e) {
            return false;
          }
        })
        .toList();
    if (filtrados.isEmpty()) {
      return respuestaFactory.armarRespuesta(pagina, List.of(), pageable);
    }
    Map<String, String> provinciaPorCoord = webClient.obtenerProvinciaPorCoordenada(filtrados);

    List<Hecho> hechos = new ArrayList<>(filtrados.size());
    String origen = desastresApiClient.getBaseUrl();
    for (HechoDTO data : filtrados) {
      String provincia = provinciaPorCoord.getOrDefault(
          keyCoord(data.getLatitud(), data.getLongitud()),
          "DESCONOCIDA"
      );

      Hecho hecho = hechoMapper.aDominio(data, provincia, origen);
      hechos.add(hecho);
    }
    return respuestaFactory.armarRespuesta(pagina, hechos, pageable);
  }


  private String keyCoord(double lat, double lon) {
    return lat + "|" + lon;
  }
}
