package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuentes.dinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.fuentes.estatica.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.proxy.FuenteProxy;
import org.springframework.stereotype.Component;

@Component
public class FuenteFactory {

  private final FuenteDinamica fuenteDinamica;
  private final FuenteEstatica fuenteEstatica;
  private final FuenteProxy fuenteProxy;

  public FuenteFactory(
      FuenteDinamica fuenteDinamica,
      FuenteEstatica fuenteEstatica,
      FuenteProxy fuenteProxy
  ) {
    this.fuenteDinamica = fuenteDinamica;
    this.fuenteEstatica = fuenteEstatica;
    this.fuenteProxy = fuenteProxy;
  }

  public IFuente aDominio(TipoFuente tipo) {
    return switch (tipo) {
      case CONTRIBUYENTE -> fuenteDinamica;
      case DATASET -> fuenteEstatica;
      case PROXY -> fuenteProxy;
    };
  }
}

