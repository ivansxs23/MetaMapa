package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.CategoriaDTO;
import java.util.List;

public interface ICategoriaService {
  List<CategoriaDTO> obtenerTodas();
}
