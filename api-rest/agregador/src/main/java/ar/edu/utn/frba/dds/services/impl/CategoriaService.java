package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.entities.Categoria;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService {

  private final ICategoriaRepository categoriaRepository;

  public CategoriaService(@Lazy ICategoriaRepository categoriaRepository) {
    this.categoriaRepository = categoriaRepository;
  }

  @Override
  public Categoria buscarOCrearCategoria(String nombre) {
    return categoriaRepository.findByNombre(nombre)
            .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
  }

  @Override
  public Categoria buscarPorId(Long id) {
    return categoriaRepository.findById(id).orElse(null);
  }

  @Override
  public List<Categoria> obtenerTodas() {
    return categoriaRepository.findAll();
  }
}
