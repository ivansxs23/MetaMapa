package ar.edu.utn.frba.dds.webclient.utils;

import ar.edu.utn.frba.dds.webclient.exceptions.DuplicateEntityException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
@Component
public class ExceptionFactory {
  public DuplicateEntityException construirExcepcionDuplicado(WebClientResponseException e) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(e.getResponseBodyAsString());
      String field = root.has("entityName") ? root.get("entityName").asText() : "username";
      String message = root.has("message") ? root.get("message").asText() : "Ya existe un registro con ese valor";
      return new DuplicateEntityException(field, message);
    } catch (Exception ex) {
      return new DuplicateEntityException("username", "Error al procesar la respuesta del servidor");
    }
  }
}
