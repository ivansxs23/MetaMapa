package ar.edu.utn.frba.dds.entities.dto.input;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {
  private String nombreFuente;
  private Long cantidadHechos;
  private LocalDateTime fechaUltimaModificacion;
}
