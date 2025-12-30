package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.filtro.FiltroDTO;
import ar.edu.utn.frba.dds.services.IHechoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/hechos")
public class HechoController {

  private final IHechoService hechoService;

  public HechoController(IHechoService iHechoService) {
    this.hechoService = iHechoService;
  }

  @GetMapping
  public Page<Hecho> obtenerHechos(
      @ModelAttribute FiltroDTO filtro,
      Pageable pageable){
    return hechoService.obtenerHechos(filtro, pageable);
  }


  @PostMapping(value = "/importar")
  public ResponseEntity<String> importHechosPorLote(@RequestParam("archivo") MultipartFile archivo){
    hechoService.importarHechosPorLote(archivo);
    return ResponseEntity.accepted().body("Archivo importado correctamente");
  }
}
