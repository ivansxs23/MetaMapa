package ar.edu.utn.frba.dds.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HoraCantidadDTO {
  private Integer hora;
  private Long cantidad;
  private String categoria;
}
