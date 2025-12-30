package ar.edu.utn.frba.dds.webclient.dto.input.coleccion;
import ar.edu.utn.frba.dds.webclient.dto.CategoriaDTO;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("categoria")
public class CriterioPorCategoriaDTO implements CriterioInputDTO {
    private String tipo;
    private CategoriaDTO categoria;

    @Override
    public String getTipo() {
        return this.tipo;
    }
}
