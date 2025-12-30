package ar.edu.utn.frba.dds.entities;

import lombok.Data;

@Data
public class Hecho {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Ubicacion ubicacion;
  private String fecha_hecho;
  private String created_at;
  private String updated_at;
  private String origen;
}
