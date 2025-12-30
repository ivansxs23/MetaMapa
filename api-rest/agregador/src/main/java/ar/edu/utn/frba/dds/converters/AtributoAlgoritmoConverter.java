package ar.edu.utn.frba.dds.converters;

import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class AtributoAlgoritmoConverter implements AttributeConverter<AlgoritmoDeConsenso, String> {

    @Override
    public String convertToDatabaseColumn(AlgoritmoDeConsenso algoritmo) {
        if(algoritmo == null) {
            return null;
        }

        String tipoAlgoritmo = "";

        if (algoritmo instanceof AlgoritmoAbsoluta)
            tipoAlgoritmo = "ABSOLUTA";
        else if (algoritmo instanceof AlgoritmoMayoriaSimple)
            tipoAlgoritmo = "SIMPLE";
        else if (algoritmo instanceof AlgoritmoMultiplesMenciones)
            tipoAlgoritmo = "MULTIPLE_MENCION";
        else if (algoritmo instanceof AlgoritmoNulo)
            tipoAlgoritmo = "NINGUNO";

        return tipoAlgoritmo;
    }

    @Override
    public AlgoritmoDeConsenso convertToEntityAttribute(String s) {
        if(s == null) {
            return null;
        }

      return switch (s) {
        case "ABSOLUTA" -> new AlgoritmoAbsoluta();
        case "SIMPLE" -> new AlgoritmoMayoriaSimple();
        case "MULTIPLE_MENCION" -> new AlgoritmoMultiplesMenciones();
        default -> new AlgoritmoNulo();
      };
    }

}
