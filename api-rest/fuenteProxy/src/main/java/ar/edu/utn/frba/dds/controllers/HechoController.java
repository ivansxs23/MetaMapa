package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.input.FiltroDTO;
import ar.edu.utn.frba.dds.dto.output.PaginatedDTO;
import ar.edu.utn.frba.dds.services.IHechoService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final IHechoService hechoService;

    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }
    @GetMapping
    public ResponseEntity<PaginatedDTO> obtenerHechos(@ModelAttribute FiltroDTO filtros, Pageable pageable){
        return ResponseEntity.ok(hechoService.obtenerHechos(filtros, pageable));
    }
}
