package ar.edu.utn.frba.dds.entities;

import ar.edu.utn.frba.dds.converters.AtributoAlgoritmoConverter;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoNulo;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coleccion")
@Getter @Setter
public class Coleccion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "handle")
  private String handle;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descripcion")
  private String descripcion;

  @ElementCollection(targetClass = TipoFuente.class)
  @CollectionTable(
      name = "coleccion_tipo_fuente",
      joinColumns = @JoinColumn(name = "coleccion_id")
  )
  @Column(name = "tipo_fuente", nullable = false)
  @Enumerated(EnumType.STRING)
  private List<TipoFuente> tiposFuente;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "coleccion_id", nullable = false)
  private List<Criterio> criterioDePertenencia;

  @Convert(converter = AtributoAlgoritmoConverter.class)
  @Column(name = "algoritmo")
  private AlgoritmoDeConsenso algoritmo;

  @OneToMany(mappedBy = "coleccion", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ColeccionHecho> coleccionHechos;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "ultima_fecha_modificacion")
  private LocalDateTime ultimaFechaModificacion;

  @Column(name = "ultima_fecha_importacion")
  private LocalDateTime ultimaFechaImportacion;


  public Coleccion() {
    this.handle = UUID.randomUUID().toString();
    this.algoritmo = new AlgoritmoNulo();
    this.tiposFuente = new ArrayList<>();
    this.coleccionHechos = new ArrayList<>();
    this.criterioDePertenencia = new ArrayList<>();
    this.fechaCreacion = LocalDateTime.now();
    this.ultimaFechaModificacion = this.fechaCreacion;
    this.ultimaFechaImportacion = LocalDateTime.of(2000,1,1,0,0); // Fecha inicial para la primera importacion
  }

  public List<Hecho> getHechos() {
    return coleccionHechos.stream()
        .map(ColeccionHecho::getHecho)
        .toList();
  }

  public void agregarHecho(Hecho hecho) {
    boolean yaExiste = this.coleccionHechos.stream()
        .anyMatch(ch -> ch.getHecho().getId() != null &&
            ch.getHecho().getId().equals(hecho.getId()));

    if (!yaExiste) {
      ColeccionHecho ch = new ColeccionHecho(hecho, this);
      this.coleccionHechos.add(ch);
    }
  }

  public void agregarCriterio(Criterio criterio) {
    this.criterioDePertenencia.add(criterio);
  }

  public void agregarFuente(TipoFuente fuente) {
    this.tiposFuente.add(fuente);
  }
  public void eliminarHechosObsoletos() {
    List<TipoFuente> fuentesValidas = this.tiposFuente.stream().toList();

    List<Criterio> criterios = this.criterioDePertenencia;

    List<ColeccionHecho> hechosAEliminar = coleccionHechos.stream()
        .filter(ch -> {
          Hecho hecho = ch.getHecho();
          boolean tieneOrigenValido = hecho.getOrigen().stream()
              .anyMatch(origen -> fuentesValidas.contains(origen.getTipoFuente()));

          boolean cumpleCriterios = criterios.isEmpty() ||
              criterios.stream().allMatch(c -> c.cumple(hecho));

          return !tieneOrigenValido || !cumpleCriterios;
        })
        .toList();

    coleccionHechos.removeAll(hechosAEliminar);
  }

}
