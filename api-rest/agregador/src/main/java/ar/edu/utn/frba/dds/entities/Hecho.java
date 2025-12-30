package ar.edu.utn.frba.dds.entities;

import ar.edu.utn.frba.dds.utils.HechosUtils;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import java.util.Objects;
import lombok.Getter;

import lombok.Setter;

@Entity
@Table(name = "hecho")
@Getter @Setter
public class Hecho {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "identificador", unique = true, nullable = false)
  private String identificador;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
  private String descripcion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id", referencedColumnName = "id")
  private Categoria categoria;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "hecho_id", nullable = false)
  private List<Etiqueta> etiquetas;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
  private Ubicacion ubicacion;

  @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Archivo> archivos;

  @Column(name = "fecha_acontecimiento")
  private LocalDateTime fechaAcontecimiento;

  @Column(name = "es_anonimo", nullable = false)
  private Boolean esAnonimo;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "hecho_id", nullable = false)
  private List<Origen> origen;

  @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL)
  private List<ColeccionHecho> coleccionHechos;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_modificacion")
  private LocalDateTime ultimaFechaModificacion;

  public Hecho() {
    this.etiquetas = new ArrayList<>();
    this.origen = new ArrayList<>();
    this.activo = true;
    this.esAnonimo = false;
    this.archivos = new ArrayList<>();
    this.fechaCreacion = LocalDateTime.now();
    this.ultimaFechaModificacion = LocalDateTime.now();
  }

  public void eliminarOrigenesPorTipo(TipoFuente tipoFuente) {
    if (this.origen == null) return;
    this.origen.removeIf(o -> o.getTipoFuente() == tipoFuente);
  }

  public void agregarEtiqueta(Etiqueta nuevaEtiqueta) {
    if (etiquetas == null) {
      etiquetas = new ArrayList<>();
    }

    boolean existe = etiquetas.stream()
        .anyMatch(e -> e.getNombre().equalsIgnoreCase(nuevaEtiqueta.getNombre()));

    if (!existe) {
      etiquetas.add(nuevaEtiqueta);
    }
  }
  public void agregarOrigen(Origen nuevoOrigen) {


    boolean existe = origen.stream()
        .anyMatch(o ->
            o.getTipoFuente() == nuevoOrigen.getTipoFuente() &&
                Objects.equals(o.getRaiz(), nuevoOrigen.getRaiz())
        );

    if (!existe) {
      origen.add(nuevoOrigen);
    }
  }


  @PrePersist
  @PreUpdate
  public void generarIdentificador() {
    this.identificador = HechosUtils.generarIdentificador(this);
  }

}
