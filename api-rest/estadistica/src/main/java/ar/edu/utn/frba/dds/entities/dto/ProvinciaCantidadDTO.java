package ar.edu.utn.frba.dds.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProvinciaCantidadDTO {
  private Long idColeccion;
  private String tituloColeccion;
  private String provincia;
  private Long cantidad;
}
