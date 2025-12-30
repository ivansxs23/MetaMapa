package ar.edu.utn.frba.dds.entities.algoritmoDeConsenso;

import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.ColeccionHecho;

import org.springframework.stereotype.Component;

@Component
public class AlgoritmoMayoriaSimple implements AlgoritmoDeConsenso {

  @Override
  public void consensuarHechos(Coleccion coleccion) {
    int totalFuentes = coleccion.getTiposFuente().size();

    for (ColeccionHecho ch : coleccion.getColeccionHechos()) {
      long cantidad = ch.getHecho().getOrigen().stream()
          .filter(o -> coleccion.getTiposFuente()
              .stream()
              .anyMatch(f -> f.equals(o.getTipoFuente())))
          .count();

      ch.setConsensuado(cantidad >= totalFuentes / 2.0);
    }
  }
}

