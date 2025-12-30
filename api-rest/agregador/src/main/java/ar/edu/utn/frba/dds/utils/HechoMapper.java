package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.NuevoHechoDTO;
import ar.edu.utn.frba.dds.entities.dto.output.ArchivoOutputDto;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import ar.edu.utn.frba.dds.entities.dto.output.NuevoHechoDinamicaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {
  private final ObjectMapper objectMapper;

  public HechoMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public HechoOutputDto aDto(Hecho hecho, String usuarioActual) {
        HechoOutputDto hechoOutputDto = new HechoOutputDto();
        hechoOutputDto.setId(hecho.getId());
        hechoOutputDto.setTitulo(hecho.getTitulo());
        hechoOutputDto.setDescripcion(hecho.getDescripcion());
        hechoOutputDto.setCategoria(new Categoria(hecho.getCategoria().getId(), hecho.getCategoria().getNombre()));
        hechoOutputDto.setEtiquetas(hecho.getEtiquetas());
        hechoOutputDto.setLatitud(hecho.getUbicacion().getLatitud());
        hechoOutputDto.setLongitud(hecho.getUbicacion().getLongitud());
        hechoOutputDto.setProvincia(hecho.getUbicacion().getProvincia());
        List<ArchivoOutputDto> archivosDto = hecho.getArchivos()
            .stream()
            .map(a -> {
                ArchivoOutputDto out = new ArchivoOutputDto();
                out.setId(a.getId());
                out.setUrl(a.getUrl());
                out.setTipo(a.getTipo());
                return out;
            })
            .toList();

        hechoOutputDto.getArchivos().addAll(archivosDto);
        hechoOutputDto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        hechoOutputDto.setFechaCarga(hecho.getFechaCreacion());
        hechoOutputDto.setEsAnonimo(hecho.getEsAnonimo());
        hechoOutputDto.setOrigen(hecho.getOrigen());
        hechoOutputDto.setEsEditable(esEditable(hecho, usuarioActual));

        return hechoOutputDto;
    }
  private Boolean esEditable(Hecho hecho, String usuarioActual) {
    if (usuarioActual == null) {
      return false;
    }

    // ¿El usuario participa como origen contribuyente?
    boolean esCreador = hecho.getOrigen().stream()
        .anyMatch(origen ->
            origen.getTipoFuente().equals(TipoFuente.CONTRIBUYENTE) && !origen.getRaiz().equals("anonymousUser") &&
                origen.getRaiz().equals(usuarioActual)
        );

    // ¿Todavía no pasaron 7 días desde que se creó?
    boolean dentroDelTiempo = hecho.getFechaCreacion()
        .plusDays(7)
        .isAfter(LocalDateTime.now());

    return esCreador && dentroDelTiempo;
  }
  public NuevoHechoDTO parsearInputHecho(String json) {
    try {
      return objectMapper.readValue(json, NuevoHechoDTO.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("JSON inválido para InputHechoDTO", e);
    }
  }
  public NuevoHechoDinamicaDTO parsearANuevoHechoDinamicaDTO(String json) {
    try {
      return objectMapper.readValue(json, NuevoHechoDinamicaDTO.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("JSON inválido para InputHechoDTO", e);
    }
  }
  public String parsearAJson(NuevoHechoDTO inputHecho) {
    try {
      return objectMapper.writeValueAsString(inputHecho);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error al convertir InputHechoDTO a JSON", e);
    }
  }

}
