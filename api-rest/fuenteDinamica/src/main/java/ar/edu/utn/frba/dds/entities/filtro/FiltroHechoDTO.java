package ar.edu.utn.frba.dds.entities.filtro;

import ar.edu.utn.frba.dds.entities.Estado;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FiltroHechoDTO {
  private Boolean vigente;
  private String titulo;
  private Long idCategoria;
  private String username;
  private Boolean esAnonimo;
  private Boolean esEditable;
  private Estado estado;
  private LocalDateTime fechaAcontecimientoDesde;
  private LocalDateTime fechaAcontecimientoHasta;
  private LocalDateTime fechaCargaDesde;
  private LocalDateTime fechaCargaHasta;
  private LocalDateTime ultimaEdicionDesde;
  private LocalDateTime ultimaEdicionHasta;
  private String provincia;
  private Double lat;
  private Double lon;
}

