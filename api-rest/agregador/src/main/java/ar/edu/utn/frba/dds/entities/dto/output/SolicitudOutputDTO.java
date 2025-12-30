package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.dto.input.NuevoHechoDTO;
import ar.edu.utn.frba.dds.entities.solicitud.EstadoSolicitud;
import ar.edu.utn.frba.dds.entities.solicitud.TipoSolicitud;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudOutputDTO {
  private Long id;
  private TipoSolicitud tipo;
  private EstadoSolicitud estado;
  private HechoOutputDto hecho;
  private NuevoHechoDTO datosNuevos;
  private String motivo;
  private String solicitante;
  private String administrador;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaResolucion;
  private Boolean esSpam;
}
