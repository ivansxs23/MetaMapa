package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/categorias")
public class CategoriaController {
  private final ICategoriaService categoriaService;

  public CategoriaController(ICategoriaService categoriaService) {
    this.categoriaService = categoriaService;
  }

  @GetMapping
  public ResponseEntity<List<Categoria>> obtenerTodas() {
    List<Categoria> colecciones = categoriaService.obtenerTodas();
    return ResponseEntity.ok(colecciones);
  }
}
