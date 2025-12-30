package ar.edu.utn.frba.dds.webclient.dto.output;

import ar.edu.utn.frba.dds.webclient.dto.TipoFuente;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.CriterioInputDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ColeccionDTO {

        private Long id;
        private String handle;
        private String titulo;
        private String descripcion;
        private List<TipoFuente> fuentes;
        private List<CriterioDTO> criterios;
        private String algoritmo;

}
