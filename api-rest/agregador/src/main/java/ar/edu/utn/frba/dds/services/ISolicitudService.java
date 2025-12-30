package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.dto.input.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.entities.dto.input.SolicitudInputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.SolicitudOutputDTO;
import java.util.List;

public interface ISolicitudService {
  void crearSolicitud(SolicitudInputDTO dto);
  List<SolicitudOutputDTO> obtenerTodas(FiltroSolicitudDTO filtroSolicitudDTO);
  void aprobarSolicitud(Long id);
  void denegarSolicitud(Long id);

}
