package ar.edu.utn.frba.dds.entities.dto.input.Criterio;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("fecha")
public class CriterioPorFechaDTO implements CriterioDTO {
    private String tipo;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;

    @Override
    public String getTipo() {
        return this.tipo;
    }
}
