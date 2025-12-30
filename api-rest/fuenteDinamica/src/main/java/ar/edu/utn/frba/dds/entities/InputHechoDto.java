package ar.edu.utn.frba.dds.entities;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InputHechoDto {
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaAcontecimiento;
    private Long idCategoria;
    private Double latitud;
    private Double longitud;
    private Boolean esAnonimo;
    private String usuario;
    private List<ArchivoDTO> archivos;
}
