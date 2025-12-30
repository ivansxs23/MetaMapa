package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Archivo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "url", nullable = false)
  private String url;

  @Enumerated(EnumType.STRING)
  @Column(name="tipo_archivo",nullable = false)
  private TipoArchivo tipo;

}
