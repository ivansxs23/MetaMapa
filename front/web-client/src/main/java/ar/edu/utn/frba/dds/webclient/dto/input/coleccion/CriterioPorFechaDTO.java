package ar.edu.utn.frba.dds.webclient.dto.input.coleccion;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonTypeName("fecha")
public class CriterioPorFechaDTO implements CriterioInputDTO {
    private String tipo;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;

    @Override
    public String getTipo() {
        return this.tipo;
    }
}
