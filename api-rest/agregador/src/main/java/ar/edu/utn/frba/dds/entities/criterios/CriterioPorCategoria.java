package ar.edu.utn.frba.dds.entities.criterios;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Hecho;

import ar.edu.utn.frba.dds.entities.filtros.FiltroPorCategoria;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("categoria")
@Getter @Setter @NoArgsConstructor
public class CriterioPorCategoria extends Criterio{
    @ManyToOne
    private Categoria categoria;


    public CriterioPorCategoria(Categoria categoria) {
        this.tipo = "categoria";
        this.categoria = categoria;
    }

    @Override
    public Boolean cumple(Hecho hecho) {
        return new FiltroPorCategoria(categoria.getId()).aplicarA(hecho);
    }
}
