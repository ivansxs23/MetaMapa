package ar.edu.utn.frba.dds.entities.dto.input;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NuevoHechoDTO {
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaAcontecimiento;
    private Long idCategoria;
    private Double latitud;
    private Double longitud;
    private Boolean esAnonimo;
    private List<ArchivoDTO> archivos;
}
