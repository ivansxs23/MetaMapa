package ar.edu.utn.frba.dds.entities.criterios;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorProvincia;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("provincia")
@Getter @Setter @NoArgsConstructor
public class CriterioPorProvincia extends Criterio{

    @Column(name = "provincia")
    private String provincia;
    public CriterioPorProvincia(String provincia) {
        this.tipo = "provincia";
        this.provincia = provincia;

    }

    @Override
    public Boolean cumple(Hecho hecho) {
        return (new FiltroPorProvincia(provincia).aplicarA(hecho));
    }
}
