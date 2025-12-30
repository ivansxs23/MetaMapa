package ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HechoDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Float latitud;
  private Float longitud;
  private String fecha_hecho;
  private String created_at;
  private String updated_at;
  private String origen;
}
