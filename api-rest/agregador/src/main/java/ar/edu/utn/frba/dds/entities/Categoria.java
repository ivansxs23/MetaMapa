package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "categoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Categoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "nombre",unique = true, nullable = false)
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }
}
