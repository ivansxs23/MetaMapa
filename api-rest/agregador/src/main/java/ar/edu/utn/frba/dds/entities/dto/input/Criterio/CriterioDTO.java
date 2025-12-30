package ar.edu.utn.frba.dds.entities.dto.input.Criterio;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioPorFechaDTO.class, name = "fecha"),
        @JsonSubTypes.Type(value = CriterioPorProvinciaDTO.class, name = "provincia"),
        @JsonSubTypes.Type(value = CriterioPorCategoriaDTO.class, name = "categoria"),
})
public interface CriterioDTO {
    String getTipo();
}