package ar.edu.utn.frba.dds.webclient.dto.input.coleccion;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("provincia")
public class CriterioPorProvinciaDTO implements CriterioInputDTO{
    private String tipo;
    private String provincia;

    @Override
    public String getTipo() {
        return this.tipo;
    }
}
