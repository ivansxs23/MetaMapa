package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data @Entity
public class Ubicacion {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double latitud;
  private Double longitud;
  private String provincia;

  public Ubicacion(){
    this.provincia = "Desconocido";
  }
}
