package ar.edu.utn.frba.dds.entities.filtros;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.util.Objects;

public class FiltroPorProvincia implements FiltroHecho {
   private final String provincia;

  public FiltroPorProvincia(String provincia) {
    this.provincia = provincia;
  }

  @Override
    public Boolean aplicarA(Hecho hecho) {
        if(hecho.getUbicacion() == null || hecho.getUbicacion().getProvincia() == null) return false;
        return Objects.equals(hecho.getUbicacion().getProvincia().toLowerCase(), provincia.toLowerCase());
    }
}
