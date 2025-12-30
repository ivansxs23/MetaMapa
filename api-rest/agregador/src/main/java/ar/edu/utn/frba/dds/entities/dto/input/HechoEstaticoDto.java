package ar.edu.utn.frba.dds.entities.dto.input;

import ar.edu.utn.frba.dds.entities.Categoria;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoEstaticoDto {
  private Long id;
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private UbicacionDTO ubicacion;
  private LocalDateTime fechaHecho;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaModificacion;
  private String origen;
}
