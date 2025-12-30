package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiltroPorCategoria implements FiltroHecho{
    private Long idCategoria;
    @Override
    public Boolean aplicarA(Hecho hecho) {
        return hecho.getCategoria() != null && hecho.getCategoria().getId().equals(this.idCategoria);
    }
}