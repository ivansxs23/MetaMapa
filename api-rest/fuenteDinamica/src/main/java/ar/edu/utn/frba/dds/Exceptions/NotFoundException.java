package ar.edu.utn.frba.dds.Exceptions;

public class NotFoundException extends RuntimeException {
  public NotFoundException(Long id) {
    super("No se encontró la categoría con ID: " + id);
  }
}

