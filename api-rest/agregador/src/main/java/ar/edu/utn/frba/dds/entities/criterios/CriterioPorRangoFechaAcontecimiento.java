package ar.edu.utn.frba.dds.entities.criterios;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorFechaAcontecimiento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("fecha")
@Getter @Setter @NoArgsConstructor
public class CriterioPorRangoFechaAcontecimiento extends Criterio{

  @Column(name="fecha_desde")
  private LocalDateTime fechaDesde;
  @Column(name="fecha_hasta")
  private LocalDateTime fechaHasta;

  public CriterioPorRangoFechaAcontecimiento(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
    this.tipo = "fecha";
    this.fechaDesde = fechaDesde;
    this.fechaHasta = fechaHasta;
  }

  @Override
    public Boolean cumple(Hecho hecho) {
        return (new FiltroPorFechaAcontecimiento(fechaDesde, fechaHasta).aplicarA(hecho));
    }
}
