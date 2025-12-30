package ar.edu.utn.frba.dds.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;
    @Column
    private String nombre;
    @Column
    private String contrasenia;
    @Column
    private String email;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Enumerated(EnumType.STRING)
    @ElementCollection()
    @CollectionTable(name = "usuario_permisos", joinColumns = @JoinColumn(name = "usuario_id"))
    private List<Permiso> permisos;

}
