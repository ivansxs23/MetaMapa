package ar.edu.utn.frba.dds.entities.dto.input.coleccion;

import java.util.List;

import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.TipoAlgoritmo;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioDTO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ColeccionDTO {
    String titulo;
    String descripcion;
    List<CriterioDTO> criterios;
    TipoAlgoritmo algoritmo;
    List<TipoFuente> fuentes;
}
