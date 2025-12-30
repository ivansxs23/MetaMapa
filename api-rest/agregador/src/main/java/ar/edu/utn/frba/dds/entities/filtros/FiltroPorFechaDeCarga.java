package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorFechaDeCarga implements FiltroHecho {
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;

    @Override
    public Boolean aplicarA(Hecho hecho) {
        FiltroPorFecha filtro = new FiltroPorFecha(fechaDesde, fechaHasta);
        return filtro.filtrar(hecho);
    }
}
