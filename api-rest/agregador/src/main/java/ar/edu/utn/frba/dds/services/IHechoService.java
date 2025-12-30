package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHechoService {
  Hecho obtenerHechoPorId(Long id);
  Page<HechoOutputDto> obtenerTodosHechos(FiltroBusquedaHechosDTO filtrosDTO,Long idCollecion, Pageable pageable);
  Hecho guardar(Hecho hecho);
  HechoOutputDto obtenerHechoOutputDtoPorId(Long id);
  void eliminarHecho(Long id);
  void guardarTodos(List<Hecho> importados);
}
