package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hecho")
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "vigente", nullable = false)
  private Boolean vigente;

  @Column(name = "titulo", nullable = false)
  private String titulo;

  @Column(name = "descripcion", nullable = false)
  private String descripcion;

  @Column(name = "categoria_id")
  private Long idCategoria;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ubicacion_id")
  private Ubicacion ubicacion;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "hecho_id")
  private List<Archivo> archivos;

  @Column(name = "fecha_acontecimiento")
  private LocalDateTime fechaAcontecimiento;

  @Column(name = "usuario")
  private String username;

  @Column(name = "es_anonimo")
  private Boolean esAnonimo;

  @Column(name = "es_editable")
  private Boolean esEditable;

  @Column(name = "estado")
  private Estado estado;

  @Column(name = "fecha_carga")
  private LocalDateTime fechaCarga;

  @Column(name = "ultima_edicion")
  private LocalDateTime ultimaEdicion;


  public Hecho() {
    this.vigente = true;
    this.fechaCarga = LocalDateTime.now();
    this.ultimaEdicion = LocalDateTime.now();
    this.archivos = new ArrayList<>();
  }
}

