package ar.edu.utn.frba.dds.webclient.dto;

import lombok.Data;

@Data
public class ProvinciaCantidadCategoria {
    private String provincia;
    private String categoria;
    private Long cantidad;
}
