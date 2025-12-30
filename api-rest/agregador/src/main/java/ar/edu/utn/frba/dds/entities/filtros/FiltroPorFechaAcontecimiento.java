package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorFechaAcontecimiento implements FiltroHecho{
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    @Override
    public Boolean aplicarA(Hecho hecho) {
        return (hecho.getFechaAcontecimiento().isAfter(fechaDesde) && hecho.getFechaAcontecimiento().isBefore(fechaHasta));
    }
}
