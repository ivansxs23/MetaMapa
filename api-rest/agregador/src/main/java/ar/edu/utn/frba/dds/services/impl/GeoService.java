package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.services.IGeoService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeoService implements IGeoService {

    private final WebClient webClient;

    public GeoService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public String obtenerProvincia(Double latitud, Double longitud) {
        String url = "https://apis.datos.gob.ar/georef/api/ubicacion" +
                "?lat=" + latitud +
                "&lon=" + longitud +
                "&aplanar=true&campos=estandar";

        Map response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
          if(!response.containsKey("ubicacion")) {
              return null;
          }
          Map address = (Map) response.get("ubicacion");
          return (String) address.get("provincia_nombre");
    }
}