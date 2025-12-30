package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.HechoCsvData;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {

  public void mapearDesdeCsv(
      Hecho hecho,
      HechoCsvData data,
      Categoria categoria,
      String nombreArchivo,
      String provincia
  ) {
    hecho.setTitulo(data.getTitulo());
    hecho.setDescripcion(data.getDescripcion());
    hecho.setCategoria(categoria);
    hecho.setFechaHecho(data.getFechaAcontecimiento().atStartOfDay());
    hecho.setOrigen(nombreArchivo);

    Ubicacion ubicacion = hecho.getUbicacion() != null
        ? hecho.getUbicacion()
        : new Ubicacion();

    ubicacion.setLatitud(data.getLatitud());
    ubicacion.setLongitud(data.getLongitud());

    if (provincia != null) {
      ubicacion.setProvincia(provincia);
    }

    hecho.setUbicacion(ubicacion);
  }

}
