package ar.edu.utn.frba.dds.webclient.dto;

import ar.edu.utn.frba.dds.webclient.dto.input.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudDTO {
    private TipoSolicitud tipo;
    private Long idHecho;
    private HechoDTO datosNuevos;
    private String motivo;
}
