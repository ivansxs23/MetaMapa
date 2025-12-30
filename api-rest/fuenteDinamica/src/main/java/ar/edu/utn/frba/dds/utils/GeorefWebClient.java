package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.geoServiceDTO.input.GeorefResultado;
import java.time.Duration;

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
  public String obtenerProvinciaPorCoordenada(Double latitud, Double longitud) {

    try {
      GeorefResultado resp = webClient
          .get()
          .uri(uriBuilder -> uriBuilder
              .path("/ubicacion")
              .queryParam("lat", latitud)
              .queryParam("lon", longitud)
              .queryParam("aplanar", true)
              .queryParam("campos", "basico")
              .build()
          )
          .retrieve()
          .bodyToMono(GeorefResultado.class)
          .block();
      if(resp != null && resp.ubicacion().provincia_nombre() != null){
        System.out.println(resp.ubicacion().provincia_nombre());
        return resp.ubicacion().provincia_nombre();
      }else{
        return "Desconocido";
      }
      } catch (Exception ex) {
      // fallback: no corto la importación; dejo provincia null y logueo
      logger.warn("GeoRef falló para el lote (se persiste sin provincia). Motivo: {}", ex.getMessage());
      return "Desconocido";
    }
  }
}
