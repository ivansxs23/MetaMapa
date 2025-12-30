package ar.edu.utn.frba.dds.webclient.dto;


import ar.edu.utn.frba.dds.webclient.dto.input.EstadoSolicitud;
import ar.edu.utn.frba.dds.webclient.dto.input.TipoSolicitud;
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

