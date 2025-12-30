package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.services.IAlgoritmoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/consensuar")
public class AlgoritmoController {
  private final IAlgoritmoService algoritmoService;

  public AlgoritmoController(IAlgoritmoService algoritmoService) {
    this.algoritmoService = algoritmoService;
  }

  @GetMapping
  public void correrAlgortimo(){
    algoritmoService.consensuarHechos();
  }
}
