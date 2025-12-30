package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoAbsoluta;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoMayoriaSimple;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoMultiplesMenciones;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoNulo;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.TipoAlgoritmo;

import org.springframework.stereotype.Component;

@Component
public class AlgoritmoFactory {

  private final AlgoritmoMayoriaSimple algoritmoMayoriaSimple;
  private final AlgoritmoMultiplesMenciones algoritmoMultiplesMenciones;
  private final AlgoritmoAbsoluta algoritmoAbsoluta;
  private final AlgoritmoNulo algoritmoNulo;

  public AlgoritmoFactory(AlgoritmoMayoriaSimple algoritmoMayoriaSimple, AlgoritmoMultiplesMenciones algoritmoMultiplesMenciones, AlgoritmoAbsoluta algoritmoAbsoluta, AlgoritmoNulo algoritmoNulo) {
    this.algoritmoMayoriaSimple = algoritmoMayoriaSimple;
    this.algoritmoMultiplesMenciones = algoritmoMultiplesMenciones;
    this.algoritmoAbsoluta = algoritmoAbsoluta;
    this.algoritmoNulo = algoritmoNulo;
  }

  public AlgoritmoDeConsenso aDominio(TipoAlgoritmo algoritmo) {
    return switch (algoritmo) {
      case SIMPLE-> algoritmoMayoriaSimple;
      case MULTIPLE_MENCION -> algoritmoMultiplesMenciones;
      case ABSOLUTA -> algoritmoAbsoluta;
      case NINGUNO -> algoritmoNulo;
    };
  }
}
