package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CriterioOutputDTO {

    private String tipo;

    private Categoria categoria;

    private String provincia;

    private LocalDateTime fechaDesde;

    private LocalDateTime fechaHasta;
}
