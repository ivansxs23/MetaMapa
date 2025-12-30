package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.dto.input.ArchivoDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class NuevoHechoDinamicaDTO {
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
