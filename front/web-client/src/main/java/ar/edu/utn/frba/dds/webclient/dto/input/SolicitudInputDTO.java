package ar.edu.utn.frba.dds.webclient.dto.input;

import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudInputDTO {
  private Long id;
  private TipoSolicitud tipo;
  private EstadoSolicitud estado;
  private HechoInputDTO hecho;
  private HechoDTO datosNuevos;
  private String motivo;
  private String solicitante;
  private String administrador;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaResolucion;
  private Boolean esSpam;
}
