package ar.edu.utn.frba.dds.webclient.dto.input.coleccion;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.TipoFuente;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

// DTO para recibir del backend
@Data
@ToString
public class ColeccionDetalladoInputDTO {
    private Long id;
    private String handle;
    private String titulo;
    private String descripcion;
    private List<TipoFuente> fuentes;
    private List<CriterioInputDTO> criterios;
    private String algoritmo;
    private List<HechoInputDTO> hechos;
}
