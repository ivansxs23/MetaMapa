package ar.edu.utn.frba.dds.entities.algoritmoDeConsenso;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.ColeccionHecho;
import org.springframework.stereotype.Component;

@Component
public class AlgoritmoNulo implements AlgoritmoDeConsenso {

    @Override
    public void consensuarHechos(Coleccion coleccion) {
        for (ColeccionHecho ch : coleccion.getColeccionHechos()) {
            ch.setConsensuado(false);
        }
    }
}
