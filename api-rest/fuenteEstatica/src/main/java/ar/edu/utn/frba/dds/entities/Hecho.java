package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "titulo")
  private String titulo;
  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;
  @ManyToOne
  @JoinColumn(name = "categoria_id", referencedColumnName = "id")
  private Categoria categoria;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ubicacion_id")
  private Ubicacion ubicacion;
  @Column(name = "fecha_hecho")
  private LocalDateTime fechaHecho;
  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;
  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;
  @Column(name = "origen")
  private String origen;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.fechaCreacion = now;
    this.fechaModificacion = now;
  }

  @PreUpdate
  protected void onUpdate() {
    this.fechaModificacion = LocalDateTime.now();
  }
}
