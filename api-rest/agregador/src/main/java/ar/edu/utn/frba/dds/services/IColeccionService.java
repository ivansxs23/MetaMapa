package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.input.coleccion.ColeccionDTO;
import ar.edu.utn.frba.dds.entities.dto.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IColeccionService {
  List<ColeccionOutputDTO> obtenerTodasColecciones();
  Coleccion buscarColeccionePorId(Long id);
  ColeccionOutputDTO obtenerColeccionPorId(Long id);
  void guardarColeccion(ColeccionDTO coleccionDto);
  Coleccion editarColeccion(Long idColeccion, ColeccionDTO coleccionDto);
  void eliminarColeccion(Long idColeccion);
  Page<HechoOutputDto> obtenerHechosDeColeccion(Long idColeccion, FiltroBusquedaHechosDTO filtros, Pageable pageable);
  void actualizarColeccion(Long coleccionId);
  void actualizarColecciones();
  void consensuarHechosDeColeccion(Long id);
}
