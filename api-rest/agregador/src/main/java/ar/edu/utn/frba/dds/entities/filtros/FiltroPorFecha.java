package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorFecha {
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    public Boolean filtrar(Hecho hecho) {
        return (hecho.getFechaCreacion().isAfter(fechaDesde) && hecho.getFechaCreacion().isBefore(fechaHasta));
    }
}
