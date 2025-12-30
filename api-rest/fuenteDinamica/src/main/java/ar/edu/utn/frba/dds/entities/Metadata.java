package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
  @Id
  private String nombreFuente;
  @Column
  private Long cantidadHechos;
  @Column
  private LocalDateTime fechaUltimaModificacion;
}
