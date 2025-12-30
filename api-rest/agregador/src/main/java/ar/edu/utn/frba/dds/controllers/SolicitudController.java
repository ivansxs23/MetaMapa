package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.dto.input.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.entities.dto.input.SolicitudInputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.services.ISolicitudService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/solicitudes")
public class SolicitudController {

  private final ISolicitudService solicitudService;

  public SolicitudController(ISolicitudService solicitudService) {
    this.solicitudService = solicitudService;
  }

  @PostMapping
  public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudInputDTO solicitudInputDTO) {
    System.out.println("Entrando solicitud:" + solicitudInputDTO.getTipo());
    solicitudService.crearSolicitud(solicitudInputDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada correctamente");
  }
  @GetMapping
  public ResponseEntity<List<SolicitudOutputDTO>> obtenerTodas(@ModelAttribute FiltroSolicitudDTO filtro) {
    return ResponseEntity.ok(solicitudService.obtenerTodas(filtro));
  }
  @GetMapping("/{id}/aprobar")
  public ResponseEntity<String> aprobarSolicitud(@PathVariable Long id) {
    solicitudService.aprobarSolicitud(id);
    return ResponseEntity.ok("Solicitud aprobada correctamente.");
  }

  @GetMapping("/{id}/denegar")
  public ResponseEntity<String> denegarSolicitud(@PathVariable Long id) {
    solicitudService.denegarSolicitud(id);
    return ResponseEntity.ok("Solicitud denegada correctamente.");
  }
}
