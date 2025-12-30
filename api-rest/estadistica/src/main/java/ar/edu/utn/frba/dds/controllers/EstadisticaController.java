package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Estadistica;
import ar.edu.utn.frba.dds.service.IEstadisticaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final IEstadisticaService service;

    public EstadisticaController(IEstadisticaService service) {
        this.service = service;
    }

    @GetMapping("/generar")
    public void generarEstadistica() {
        service.generarEstadisticas();
    }
    @GetMapping()
    public ResponseEntity<List<Estadistica>> obtenerEstadisticas() {
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    @GetMapping("/ultima")
    public ResponseEntity<Estadistica> obtenerUltima() {
        List<Estadistica> lista = service.obtenerEstadisticas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista.get(lista.size() - 1));
    }


    //TODO: exportar estadisticas en CSV
    @GetMapping(value="/export", produces = "text/csv")
    public ResponseEntity<StreamingResponseBody> exportCsv() {
        return null; /*service.exportCsv();*/
    }
}
