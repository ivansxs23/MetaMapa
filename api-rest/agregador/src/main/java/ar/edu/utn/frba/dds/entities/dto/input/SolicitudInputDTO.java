package ar.edu.utn.frba.dds.entities.dto.input;

import ar.edu.utn.frba.dds.entities.solicitud.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudInputDTO {
  private TipoSolicitud tipo;
  private Long idHecho;
  private NuevoHechoDTO datosNuevos;
  private String motivo;
}
