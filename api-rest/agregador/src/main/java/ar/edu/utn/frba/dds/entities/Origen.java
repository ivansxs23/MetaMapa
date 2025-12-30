package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "origen")
@Getter @Setter @NoArgsConstructor
public class Origen {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_fuente", nullable = false)
  private TipoFuente tipoFuente;

  @Column(name = "id_en_fuente")
  private Long idEnFuente;

  @Column(name = "raiz_fuente")
  private String raiz;


  public Origen(TipoFuente tipoFuente, Long idEnFuente, String raiz) {
    this.tipoFuente = tipoFuente;
    this.idEnFuente = idEnFuente;
    this.raiz = raiz;
  }
}
