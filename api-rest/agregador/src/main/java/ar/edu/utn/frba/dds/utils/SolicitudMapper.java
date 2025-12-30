package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.dto.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.entities.solicitud.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {
  private final HechoMapper hechoMapper;

  public SolicitudMapper(HechoMapper hechoMapper) {
    this.hechoMapper = hechoMapper;
  }

  public SolicitudOutputDTO aDto(Solicitud solicitud) {
    SolicitudOutputDTO dto = new SolicitudOutputDTO();
    dto.setId(solicitud.getId());
    dto.setTipo(solicitud.getTipo());
    dto.setEstado(solicitud.getEstado());
    if(solicitud.getHecho() != null) {
      dto.setHecho(hechoMapper.aDto(solicitud.getHecho(), null));
    }
    if(solicitud.getDatosNuevos() != null) {
      dto.setDatosNuevos(hechoMapper.parsearInputHecho(solicitud.getDatosNuevos()));
    }
    dto.setMotivo(solicitud.getMotivo());
    dto.setSolicitante(solicitud.getSolicitante());
    dto.setAdministrador(solicitud.getAdministrador());
    dto.setFechaCreacion(solicitud.getFechaCreacion());
    dto.setFechaResolucion(solicitud.getFechaResolucion());
    dto.setEsSpam(solicitud.getEsSpam());
    return dto;
  }
}
