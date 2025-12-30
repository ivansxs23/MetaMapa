package ar.edu.utn.frba.dds.entities.dto.input;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FiltroBusquedaHechosDTO {
  private Long idCategoria;
  private String provincia;
  private String fuente;
  private LocalDateTime fechaAcontecimientoDesde;
  private LocalDateTime fechaAcontecimientoHasta;
  private LocalDateTime ultimaFechaModificacionDesde;
  private Boolean esAnonimo;
  private Boolean curado;
  private Boolean activo;
  private Long idColeccion;
}
