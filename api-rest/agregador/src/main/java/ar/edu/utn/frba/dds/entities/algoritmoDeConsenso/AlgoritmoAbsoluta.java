package ar.edu.utn.frba.dds.entities.algoritmoDeConsenso;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.ColeccionHecho;

import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AlgoritmoAbsoluta implements AlgoritmoDeConsenso{

  @Override
  public void consensuarHechos(Coleccion coleccion) {
    Set<TipoFuente> fuentesColeccion = new HashSet<>(coleccion.getTiposFuente());

    for (ColeccionHecho ch : coleccion.getColeccionHechos()) {
      Set<TipoFuente> fuentesDelHecho = ch.getHecho().getOrigen().stream()
          .map(Origen::getTipoFuente)
          .collect(Collectors.toSet());

      ch.setConsensuado(fuentesDelHecho.containsAll(fuentesColeccion));
    }
  }
}
