package ar.edu.utn.frba.dds.entities.dto.input;

import ar.edu.utn.frba.dds.entities.solicitud.EstadoSolicitud;
import ar.edu.utn.frba.dds.entities.solicitud.TipoSolicitud;
import lombok.Data;

@Data
public class FiltroSolicitudDTO {
  private Long id;
  private TipoSolicitud tipo;
  private EstadoSolicitud estado;
  private Long hechoId;
  private Boolean esSpam;
  private String solicitante;
  private String administrador;
}

