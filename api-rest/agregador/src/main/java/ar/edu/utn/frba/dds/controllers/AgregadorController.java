package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.services.impl.AgregadorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/importar")
public class AgregadorController {
  private final AgregadorService agregadorService;

  public AgregadorController(AgregadorService agregadorService) {
    this.agregadorService = agregadorService;
  }
  @GetMapping
  public void importarYactualizar(){
    agregadorService.importarHechosYActualizarColecciones();
  }
}
