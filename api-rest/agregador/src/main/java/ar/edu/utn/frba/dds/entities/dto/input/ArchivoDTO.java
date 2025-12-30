package ar.edu.utn.frba.dds.entities.dto.input;

import ar.edu.utn.frba.dds.entities.TipoArchivo;
import lombok.Data;

@Data
public class ArchivoDTO {
  private Long id;
  private String url;
  private TipoArchivo tipo;
}
