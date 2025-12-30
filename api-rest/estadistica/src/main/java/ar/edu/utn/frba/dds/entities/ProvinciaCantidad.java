package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "provincia_cantidad")
public class ProvinciaCantidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idColeccion;
    private String tituloColeccion;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "cantidad")
    private Long cantidad;
}
