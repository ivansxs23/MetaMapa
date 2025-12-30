package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

@Entity @Data
public class Archivo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private LocalDateTime fechaCreacion;
  private LocalDateTime ultimaModificacion;
  @Enumerated(EnumType.STRING)
  private Estado estado;

  public Archivo() {
    this.fechaCreacion = LocalDateTime.now();
    this.estado = Estado.PENDIENTE;
  }
}
