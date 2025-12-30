package ar.edu.utn.frba.dds.exceptions;


import lombok.Getter;

public class EntityAlreadyExistsException extends RuntimeException {
  @Getter
  private final String entityName;
  public EntityAlreadyExistsException(String entityName) {
    super(entityName + " ya existe.");
    this.entityName = entityName;
  }

}
