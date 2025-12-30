package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.entities.filtro.FiltroHechoDTO;
import ar.edu.utn.frba.dds.services.IHechoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/hechos")
public class HechoController {

  private final IHechoService hechoService;

  public HechoController(IHechoService hechoService) {
    this.hechoService = hechoService;
  }
  @GetMapping
  public ResponseEntity<Page<Hecho>> obtenerHechos(@ModelAttribute FiltroHechoDTO filtro, Pageable pageable){
    return ResponseEntity.ok(hechoService.buscar(filtro, pageable));
  }
  @PostMapping
  public ResponseEntity<Hecho> crearHecho(@RequestBody InputHechoDto nuevoHecho){

    Hecho hecho = hechoService.agregarHecho(nuevoHecho);
    return ResponseEntity.status(HttpStatus.CREATED).body(hecho);
  }
  @PutMapping("/{id}")
  public ResponseEntity<Hecho> editarHecho(@RequestBody InputHechoDto nuevoHecho, @PathVariable Long id){

    Hecho hecho = hechoService.modificarHecho(id, nuevoHecho);
    return ResponseEntity.ok(hecho);
  }
  @PutMapping("/{id}/aprobar")
  public void aprobarHecho(@PathVariable Long id) {
    this.hechoService.aprobarHecho(id);
  }
  @PutMapping("/{id}/rechazar")
  public void rechazarHecho(@PathVariable Long id) {
    this.hechoService.rechazarHecho(id);
  }
}

