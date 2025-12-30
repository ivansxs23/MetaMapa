package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.HechoCsvData;
import ar.edu.utn.frba.dds.entities.dto.geoServiceDTO.input.GeorefResultado;
import ar.edu.utn.frba.dds.entities.dto.geoServiceDTO.input.GeorefUbicacionesResponse;
import ar.edu.utn.frba.dds.entities.dto.geoServiceDTO.output.GeorefUbicacion;
import ar.edu.utn.frba.dds.entities.dto.geoServiceDTO.output.GeorefUbicacionesRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
@Component
public class GeorefWebClient implements IWebClient {
  private final WebClient webClient;
  private static final Logger logger = LoggerFactory.getLogger(GeorefWebClient.class);

  public GeorefWebClient(WebClient.Builder builder) {
    this.webClient = builder
        .baseUrl("https://apis.datos.gob.ar/georef/api")
        .build();
  }
  public Map<String, String> obtenerProvinciaPorCoordenada(List<HechoCsvData> lote) {

    // deduplico coordenadas dentro del lote
    Map<String, GeorefUbicacion> itemsPorKey = new LinkedHashMap<>();
    for (HechoCsvData d : lote) {
      String key = keyCoord(d.getLatitud(), d.getLongitud());
      itemsPorKey.putIfAbsent(key, new GeorefUbicacion(
          d.getLatitud(),
          d.getLongitud(),
          true,
          "basico"
      ));
    }

    GeorefUbicacionesRequest req = new GeorefUbicacionesRequest(new ArrayList<>(itemsPorKey.values()));

    try {
      GeorefUbicacionesResponse resp = webClient
          .post()
          .uri("/ubicacion")
          .bodyValue(req)
          .retrieve()
          .bodyToMono(GeorefUbicacionesResponse.class)
          .timeout(Duration.ofSeconds(10))
          .block();

      Map<String, String> provinciaPorKey = new HashMap<>();
      if (resp != null && resp.resultados() != null) {
        for (GeorefResultado r : resp.resultados()) {
          if (r != null && r.ubicacion() != null) {
            String key = keyCoord(r.ubicacion().lat(), r.ubicacion().lon());
            String provincia = r.ubicacion().provincia_nombre();
            provinciaPorKey.put(key, provincia != null ? provincia : "desconocido");
          }
        }
      }
      return provinciaPorKey;

    } catch (Exception ex) {
      // fallback: no corto la importación; dejo provincia null y logueo
      logger.warn("GeoRef falló para el lote (se persiste sin provincia). Motivo: {}", ex.getMessage());
      return Collections.emptyMap();
    }
  }

  private String keyCoord(double lat, double lon) {
    return lat + "|" + lon;
  }

}
