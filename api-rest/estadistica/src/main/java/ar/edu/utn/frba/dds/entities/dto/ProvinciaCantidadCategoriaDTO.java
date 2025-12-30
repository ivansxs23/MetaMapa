package ar.edu.utn.frba.dds.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProvinciaCantidadCategoriaDTO {
    private String provincia;
    private Long cantidad;
    private String categoria;
}
