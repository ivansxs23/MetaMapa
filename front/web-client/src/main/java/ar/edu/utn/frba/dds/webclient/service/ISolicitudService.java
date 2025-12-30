package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.SolicitudDTO;

import ar.edu.utn.frba.dds.webclient.dto.input.SolicitudInputDTO;
import java.util.List;

public interface ISolicitudService {

    List<SolicitudInputDTO> obtenerTodas(FiltroSolicitudDTO filtros);

    void aprobarSolicitud(Long id);

    void denegarSolicitud(Long id);

    void crearSolicitudDeEliminacion(SolicitudDTO solicitudDTO);
    void crearSolicitudDeCreacion(HechoDTO hechoDTO);
    void crearSolicitudDeEdicion(Long id, HechoDTO hechoDTO);

}
