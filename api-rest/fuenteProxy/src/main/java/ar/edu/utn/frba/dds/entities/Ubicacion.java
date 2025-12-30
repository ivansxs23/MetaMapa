package ar.edu.utn.frba.dds.entities;

import lombok.Data;

@Data
public class Ubicacion {
  private Double latitud;
  private Double longitud;
  private String provincia;
}
