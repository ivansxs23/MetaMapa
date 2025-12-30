package ar.edu.utn.frba.dds.entities.dto.input.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvinciaCantidadDTO {
  private Long idColeccion;
  private String tituloColeccion;
  private String provincia;
  private Long cantidad;
}
