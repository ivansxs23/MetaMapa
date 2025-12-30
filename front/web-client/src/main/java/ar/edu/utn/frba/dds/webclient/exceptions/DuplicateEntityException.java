package ar.edu.utn.frba.dds.webclient.exceptions;

import lombok.Getter;

@Getter
public class DuplicateEntityException extends RuntimeException {
  @Getter
  private final String fieldName;
  public DuplicateEntityException(String fieldName, String message) {
    super(message);
    this.fieldName = fieldName;
  }
}
