package ar.edu.utn.frba.dds.entities.dto.input.ProxyDto;

import ar.edu.utn.frba.dds.entities.dto.input.UbicacionDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoProxyDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionProxyDto ubicacion;
    private String fecha_hecho;
    private String created_at;
    private String updated_at;
    private String origen;
}