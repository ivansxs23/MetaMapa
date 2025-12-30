package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.ArchivoDTO;
import ar.edu.utn.frba.dds.entities.Estado;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.entities.Archivo;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import org.springframework.stereotype.Component;

@Component
public class HechoFactory {

  public Hecho createFromDto(InputHechoDto dto, String provincia) {
    boolean anonimo = true;
    boolean editable = false;
    String usernameAGuardar = "anonymousUser";

    if (dto.getUsuario() != null && !dto.getUsuario().equals("anonymousUser")) {
      anonimo = dto.getEsAnonimo();
      editable = true;
      usernameAGuardar = dto.getUsuario();
    }

    Hecho h = new Hecho();
    h.setTitulo(dto.getTitulo());
    h.setDescripcion(dto.getDescripcion());
    h.setIdCategoria(dto.getIdCategoria());
    h.setFechaAcontecimiento(dto.getFechaAcontecimiento());
    h.setUsername(usernameAGuardar);
    h.setEsAnonimo(anonimo);
    h.setEsEditable(editable);
    h.setEstado(Estado.PENDIENTE);

    Ubicacion ubicacion = new Ubicacion();
    ubicacion.setLatitud(dto.getLatitud());
    ubicacion.setLongitud(dto.getLongitud());
    ubicacion.setProvincia(provincia);

    h.setUbicacion(ubicacion);

    // Crear medias
    if (dto.getArchivos() != null) {
      for (ArchivoDTO archivoDto : dto.getArchivos()) {

        Archivo archivo = new Archivo();
        archivo.setUrl(archivoDto.getUrl());
        archivo.setTipo(archivoDto.getTipo());
        h.getArchivos().add(archivo);
      }
    }

    return h;
  }
}

