package ar.edu.utn.frba.dds.entities.filtros;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RangoFecha {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

}
