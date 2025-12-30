package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.dto.output.EstadisticasOutputDTO;
import ar.edu.utn.frba.dds.services.IEstadisticaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/estadisticas")
public class EstadisticaController {
  private final IEstadisticaService estadisticaService;

  public EstadisticaController(IEstadisticaService estadisticaService) {
    this.estadisticaService = estadisticaService;
  }
@GetMapping
  public EstadisticasOutputDTO obtenerEstadisticas() {
    return estadisticaService.obtenerEstadisticas();
  }
}
