package ar.edu.utn.frba.dds.entities.dto.input.Criterio;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("categoria")
public class CriterioPorCategoriaDTO implements CriterioDTO {
    private String tipo;
    private Long idCategoria;

    @Override
    public String getTipo() {
        return this.tipo;
    }
}
