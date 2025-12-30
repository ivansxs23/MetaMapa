package ar.edu.utn.frba.dds.entities.algoritmoDeConsenso;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.ColeccionHecho;
import ar.edu.utn.frba.dds.entities.Hecho;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AlgoritmoMultiplesMenciones implements AlgoritmoDeConsenso{

  @Override
  public void consensuarHechos(Coleccion coleccion) {
    List<Hecho> hechos = coleccion.getHechos();
    for (ColeccionHecho ch: coleccion.getColeccionHechos()) {
      Hecho hechoAConsensuar = ch.getHecho();
      boolean tieneAlmenosDosFuentes = hechoAConsensuar.getOrigen().size() >= 2;
      boolean hayConflictoPorTitulo = seRepite(hechoAConsensuar, hechos);
      ch.setConsensuado(tieneAlmenosDosFuentes && !hayConflictoPorTitulo);
    }
    }

  private Boolean seRepite(Hecho hecho, List<Hecho> hechos){
    return hechos.stream()
        .filter(h -> h.getTitulo().equals(hecho.getTitulo()))
        .count() >1;
  }
}



