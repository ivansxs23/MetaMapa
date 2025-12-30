package ar.edu.utn.frba.dds.excepciones;

public class RequestAlreadyResolvedException extends RuntimeException {
  public RequestAlreadyResolvedException(String message) {
    super(message);
  }
}
