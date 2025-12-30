package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categoria_cantidad")
public class CategoriaCantidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "cantidad")
    private Long cantidad;

}
