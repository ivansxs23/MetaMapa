package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coleccion_hecho", uniqueConstraints = @UniqueConstraint(name = "uk_coleccion_hecho", columnNames = {"coleccion_id", "hecho_id"}))
@Getter @Setter @NoArgsConstructor
public class ColeccionHecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="coleccion_id", nullable=false)
    private Coleccion coleccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="hecho_id", nullable=false)
    private Hecho hecho;

    @Column(name = "consensuado", nullable = false)
    private Boolean consensuado;

    public ColeccionHecho(Hecho hecho, Coleccion coleccion) {
        this.hecho = hecho;
        this.consensuado = false;
        this.coleccion = coleccion;
    }
}
