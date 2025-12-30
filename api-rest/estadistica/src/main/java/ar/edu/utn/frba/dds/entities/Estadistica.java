package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "estadisticas")
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadistica_id")
    private List<ProvinciaCantidad> topProvincia;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadistica_id")
    private CategoriaCantidad topCategoria;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadistica_id")
    private List<ProvinciaCantidadCategoria> provinciaPorCategoria;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadistica_id")
    private List<HoraCantidad> horaPicoPorCategoria;
    @Column(name = "cantidad_spam")
    private Long cantidadSpam;

    public Estadistica(){
        this. topProvincia = new ArrayList<>();
        this.provinciaPorCategoria = new ArrayList<>();
        this.horaPicoPorCategoria = new ArrayList<>();

    }
}