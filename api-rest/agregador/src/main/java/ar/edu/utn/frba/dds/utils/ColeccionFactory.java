package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.entities.dto.input.coleccion.ColeccionDTO;
import org.springframework.stereotype.Component;

@Component
public class ColeccionFactory {
    private final AlgoritmoFactory algoritmoFactory;
    private final CriterioFactory criterioFactory;

    public ColeccionFactory(AlgoritmoFactory algoritmoFactory, CriterioFactory criterioFactory) {
        this.algoritmoFactory = algoritmoFactory;
        this.criterioFactory = criterioFactory;
    }

    public Coleccion aDominio(ColeccionDTO coleccionDto) {
        Coleccion coleccion = new Coleccion();
        coleccion.setTitulo(coleccionDto.getTitulo());
        coleccion.setDescripcion(coleccionDto.getDescripcion());

        if (coleccionDto.getCriterios() != null) {
            for (var criterioDto : coleccionDto.getCriterios()) {
                Criterio criterio = criterioFactory.aDominio(criterioDto);
                coleccion.agregarCriterio(criterio);
            }
        }

        if (coleccionDto.getFuentes() != null) {
            for (var tipoFuente : coleccionDto.getFuentes()) {
                coleccion.agregarFuente(tipoFuente);
            }
        }

        if (coleccionDto.getAlgoritmo() != null) {
            var algoritmo = algoritmoFactory.aDominio(coleccionDto.getAlgoritmo());
            coleccion.setAlgoritmo(algoritmo);
        }

        return coleccion;

    }
}
