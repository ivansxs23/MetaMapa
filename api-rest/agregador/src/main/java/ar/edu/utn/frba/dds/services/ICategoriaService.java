package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.Categoria;
import java.util.List;

public interface ICategoriaService {
  Categoria buscarOCrearCategoria(String nombre);
  List<Categoria> obtenerTodas();
  Categoria buscarPorId(Long id);
}
