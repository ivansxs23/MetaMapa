package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.TipoArchivo;
import lombok.Data;

@Data
public class ArchivoOutputDto {
  private Long id;
  private String url;
  private TipoArchivo tipo;
}
