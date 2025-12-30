package ar.edu.utn.frba.dds.webclient.dto.input.hecho;

import ar.edu.utn.frba.dds.webclient.dto.TipoMedia;
import lombok.Data;

@Data
public class ArchivoInputDTO {
  private Long id;
  private String url;
  private TipoMedia tipo;
}
