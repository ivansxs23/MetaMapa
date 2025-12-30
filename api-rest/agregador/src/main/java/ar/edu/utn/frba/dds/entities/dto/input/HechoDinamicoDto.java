package ar.edu.utn.frba.dds.entities.dto.input;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HechoDinamicoDto {
  private Long id;
  private Boolean vigente;
  private String titulo;
  private String descripcion;
  private Long idCategoria;
  private UbicacionDTO ubicacion;
  private List<ArchivoDTO> archivos;
  private LocalDateTime fechaAcontecimiento;
  private String username;
  private Boolean esAnonimo;
  private LocalDateTime fechaCarga;
  private LocalDateTime ultimaEdicion;

  public HechoDinamicoDto() {
    this.archivos = new ArrayList<>();
  }
}
