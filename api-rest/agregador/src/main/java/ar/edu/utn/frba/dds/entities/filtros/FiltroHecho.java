package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;

public interface FiltroHecho {
    Boolean aplicarA(Hecho hecho);
}
