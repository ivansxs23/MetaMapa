package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import ar.edu.utn.frba.dds.services.IHechoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/hechos")
public class HechosController {

  private final IHechoService hechoService;

  public HechosController(IHechoService hechoService) {
    this.hechoService = hechoService;
  }

  @GetMapping
  public ResponseEntity<Page<HechoOutputDto>> getHechos(@ModelAttribute FiltroBusquedaHechosDTO filtros, Pageable pageable) {
    return ResponseEntity.ok(hechoService.obtenerTodosHechos(filtros,null, pageable));
  }

  @GetMapping("/{id}")
  public HechoOutputDto getHechoById(@PathVariable Long id) {
    HechoOutputDto hecho = hechoService.obtenerHechoOutputDtoPorId(id);
    System.out.println(hecho.getEsEditable());
    return hecho;
  }

}
