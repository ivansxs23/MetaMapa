package ar.edu.utn.frba.dds.excepciones;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String message) {
    super(message);

  }
}
