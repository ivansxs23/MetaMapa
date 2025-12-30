package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Origen;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorOrigen implements FiltroHecho {
    private Origen origen;

    @Override
    public Boolean aplicarA(Hecho hecho) {
        return hecho.getOrigen().stream().anyMatch(o -> o.getTipoFuente().equals(this.origen.getTipoFuente()) || o.getRaiz().equals(this.origen.getRaiz()));
    }
}
