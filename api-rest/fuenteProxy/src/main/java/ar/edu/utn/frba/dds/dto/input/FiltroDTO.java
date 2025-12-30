package ar.edu.utn.frba.dds.dto.input;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FiltroDTO {
  private Long id;
  private String titulo;
  private Long categoriaId;
  private Long ubicacionId;
  private String provincia;
  private LocalDateTime fechaHechoDesde;
  private LocalDateTime fechaHechoHasta;
  private LocalDateTime fechaModificacionDesde;
  private String origen;
}

