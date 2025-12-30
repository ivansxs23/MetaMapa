package ar.edu.utn.frba.dds.entities.dto.input.estadistica;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoriaCantidadDTO {
  private String categoria;
  private Long cantidad;
}
