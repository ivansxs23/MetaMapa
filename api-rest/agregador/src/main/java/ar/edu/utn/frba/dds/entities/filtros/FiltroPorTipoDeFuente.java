package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorTipoDeFuente implements FiltroHecho {
    private TipoFuente tipoFuente;

    @Override
    public Boolean aplicarA(Hecho hecho) {
        return hecho.getOrigen().stream().anyMatch(o -> o.getTipoFuente().equals(this.tipoFuente));
    }
}
