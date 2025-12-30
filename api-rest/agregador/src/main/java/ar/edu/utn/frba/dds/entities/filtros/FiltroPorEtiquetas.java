package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FiltroPorEtiquetas implements FiltroHecho {
    private List<Long> idsEtiquetas;

    @Override
    public Boolean aplicarA(Hecho hecho) {
        return hecho.getEtiquetas().stream()
                .anyMatch(etiqueta -> this.idsEtiquetas.contains(etiqueta.getId()));
    }
}
