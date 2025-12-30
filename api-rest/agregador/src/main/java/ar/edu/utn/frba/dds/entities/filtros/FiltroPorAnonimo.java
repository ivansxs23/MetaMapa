package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorAnonimo implements FiltroHecho{
    private Boolean esAnonimo;

    @Override
    public Boolean aplicarA(Hecho hecho) {
        return hecho.getEsAnonimo().equals(this.esAnonimo);
    }
}