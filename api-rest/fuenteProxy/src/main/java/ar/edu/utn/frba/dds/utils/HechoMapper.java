package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.HechoDTO;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {
  public Hecho aDominio(HechoDTO dto, String provincia, String origen){
    Hecho hecho = new Hecho();
    hecho.setId(dto.getId());
    hecho.setTitulo(dto.getTitulo());
    hecho.setDescripcion(dto.getDescripcion());
    hecho.setCategoria(dto.getCategoria());
    hecho.setFecha_hecho(dto.getFecha_hecho());
    hecho.setCreated_at(dto.getCreated_at());
    hecho.setUpdated_at(dto.getUpdated_at());

    Ubicacion ubicacion = new Ubicacion();
    ubicacion.setLatitud(Double.valueOf(dto.getLatitud()));
    ubicacion.setLongitud(Double.valueOf(dto.getLongitud()));
    ubicacion.setProvincia(Objects.requireNonNullElse(provincia, "Desconocido"));

    hecho.setUbicacion(ubicacion);
    hecho.setOrigen(origen);


    return hecho;
  }
}
