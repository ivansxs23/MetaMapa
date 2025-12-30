package ar.edu.utn.frba.dds.entities.dto.input;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FuenteResponse {
  private Boolean islast;
  private List<Hecho> hechos;
}
