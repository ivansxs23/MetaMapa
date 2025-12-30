package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionDetalladoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.output.ColeccionDTO;


import java.util.List;

public interface IColeccionService {

    List<ColeccionInputDTO> obtenerTodasColecciones();
    ColeccionDetalladoInputDTO obtenerColeccionPorId(Long id);
    String crearColeccion(ColeccionDTO dto);
    void editarColeccion(Long id, ColeccionDTO dto);
    void eliminarColeccion(Long id);
    RestPageResponse<HechoInputDTO> obtenerHechosDeColeccion(Long id,int page, int size, FiltroDTO filtro);
    void actualizarTodasColecciones();
}
