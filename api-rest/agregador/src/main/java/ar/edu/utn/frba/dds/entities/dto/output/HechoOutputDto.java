package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Etiqueta;
import ar.edu.utn.frba.dds.entities.Origen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HechoOutputDto {
    public HechoOutputDto() {
        this.etiquetas = new ArrayList<>();
        this.origen = new ArrayList<>();
        this.archivos = new ArrayList<>();
    }

    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private List<Etiqueta> etiquetas;
    private Double latitud;
    private Double longitud;
    private List<ArchivoOutputDto> archivos;
    private String provincia;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Boolean esAnonimo;
    private List<Origen> origen;
    private Boolean esEditable;
}
