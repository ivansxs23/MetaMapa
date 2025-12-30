package ar.edu.utn.frba.dds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hora_cantidad_categoria")
@NoArgsConstructor
public class HoraCantidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hora")
    private Integer hora;
    @Column(name = "cantidad")
    private Long cantidad;
    @Column(name = "categoria")
    private String categoria;
}
