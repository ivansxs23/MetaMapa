package ar.edu.utn.frba.dds.services;
import ar.edu.utn.frba.dds.entities.Hecho;

import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.entities.filtro.FiltroHechoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHechoService {
  Page<Hecho> buscar(FiltroHechoDTO filtro, Pageable pageable);
  Hecho agregarHecho(InputHechoDto nuevoHecho);
  Hecho modificarHecho(Long id,InputHechoDto nuevoHecho);
  void aprobarHecho(Long id);
  void rechazarHecho(Long id);
}
