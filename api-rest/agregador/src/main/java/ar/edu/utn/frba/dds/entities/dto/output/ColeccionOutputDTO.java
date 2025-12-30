package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ColeccionOutputDTO {
    public ColeccionOutputDTO(){
        this.fuentes = new ArrayList<>();
        this.criterios = new ArrayList<>();
    }

    private Long id;
    private String handle;
    private String titulo;
    private String descripcion;
    private List<TipoFuente> fuentes;
    private List<Criterio> criterios;
    private String algoritmo;
}
