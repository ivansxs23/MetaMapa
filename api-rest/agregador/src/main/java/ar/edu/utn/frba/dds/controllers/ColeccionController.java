package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.input.coleccion.ColeccionDTO;
import ar.edu.utn.frba.dds.entities.dto.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import ar.edu.utn.frba.dds.services.IColeccionService;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/colecciones")
public class ColeccionController {

  private final IColeccionService coleccionService;

  public ColeccionController(IColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }
  @GetMapping
  public ResponseEntity<List<ColeccionOutputDTO>> obtenerTodas() {
    List<ColeccionOutputDTO> colecciones = coleccionService.obtenerTodasColecciones();
    return ResponseEntity.ok(colecciones);
  }
  @GetMapping("/{idColeccion}")
  public ResponseEntity<ColeccionOutputDTO> obtenerPorId(@PathVariable Long idColeccion) {
    ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(idColeccion);
    return ResponseEntity.ok(coleccion);
  }
  @GetMapping("/{idColeccion}/hechos")
  public ResponseEntity<Page<HechoOutputDto>> obtenerHechos(@PathVariable Long idColeccion, @ModelAttribute FiltroBusquedaHechosDTO filtros, Pageable pageable) {
    Page<HechoOutputDto> hechos = coleccionService.obtenerHechosDeColeccion(idColeccion, filtros, pageable);
    return ResponseEntity.ok(hechos);
  }

  @PostMapping
  public ResponseEntity<String> crear(@RequestBody ColeccionDTO coleccionDto) {
    coleccionService.guardarColeccion(coleccionDto);
    System.out.println("Criterios: "+coleccionDto.getCriterios());
    return ResponseEntity.status(HttpStatus.CREATED).body("Colección creada exitosamente.");
  }

  @PatchMapping("/{idColeccion}")
  public ResponseEntity<String> editar(@PathVariable Long idColeccion, @RequestBody ColeccionDTO coleccionDto) {
    Coleccion coleccion = coleccionService.editarColeccion(idColeccion, coleccionDto);
    return ResponseEntity.ok("Colección actualizada exitosamente.");
  }
  @DeleteMapping("/{idColeccion}")
  public ResponseEntity<String> eliminar(@PathVariable Long idColeccion) {
    coleccionService.eliminarColeccion(idColeccion);
    return ResponseEntity.ok("Colección eliminada exitosamente.");
  }

  //Borrar despues
  @GetMapping("/actualizar")
    public ResponseEntity<String> actualizar() {
        coleccionService.actualizarColecciones();
        return ResponseEntity.ok("Colecciones actualizadas exitosamente.");
    }

}
