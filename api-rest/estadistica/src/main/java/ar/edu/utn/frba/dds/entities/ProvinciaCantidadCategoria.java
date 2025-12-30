package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "provincia_cantidad_categoria")
public class ProvinciaCantidadCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "cantidad")
    private long cantidad;
    @Column(name = "categoria")
    private String categoria;
}
