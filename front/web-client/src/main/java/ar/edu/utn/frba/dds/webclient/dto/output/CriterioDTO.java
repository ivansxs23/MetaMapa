package ar.edu.utn.frba.dds.webclient.dto.output;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CriterioDTO {
    private String tipo;
    private String provincia;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private Long idCategoria;
}
