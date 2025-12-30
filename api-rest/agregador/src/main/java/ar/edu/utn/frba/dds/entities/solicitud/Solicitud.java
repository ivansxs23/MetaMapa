package ar.edu.utn.frba.dds.entities.solicitud;

import ar.edu.utn.frba.dds.entities.Hecho;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity @Table(name = "solicitudes")
@Data @NoArgsConstructor
public class Solicitud {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipo;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @ManyToOne
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;

    @Lob
    private String datosNuevos;

    private String motivo;

    private String solicitante;

    private String administrador;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaResolucion;

    private Boolean esSpam;

}
