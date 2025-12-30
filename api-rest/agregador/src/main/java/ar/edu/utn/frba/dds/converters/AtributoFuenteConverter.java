package ar.edu.utn.frba.dds.converters;

import ar.edu.utn.frba.dds.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuentes.dinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.fuentes.estatica.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.proxy.FuenteProxy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class AtributoFuenteConverter implements AttributeConverter<IFuente, String>{

    private final FuenteDinamica fuenteDinamica;
    private final FuenteProxy fuenteProxy;
    private final FuenteEstatica fuenteEstatica;

    public AtributoFuenteConverter(FuenteDinamica fuenteDinamica, FuenteProxy fuenteProxy, FuenteEstatica fuenteEstatica) {
        this.fuenteDinamica = fuenteDinamica;
        this.fuenteProxy = fuenteProxy;
        this.fuenteEstatica = fuenteEstatica;
    }

    @Override
    public String convertToDatabaseColumn(IFuente fuente) {

        if(fuente == null) {
            return null;
        }

        String tipoFuente = "";

        if(fuente instanceof FuenteDinamica) {
            tipoFuente = "CONTRIBUYENTE";
        }
        else if (fuente instanceof FuenteEstatica) {
            tipoFuente = "DATASET";
        }
        else if(fuente instanceof FuenteProxy) {
            tipoFuente = "PROXY";
        }

        return tipoFuente;
    }

    @Override
    public IFuente convertToEntityAttribute(String s) {

        if(s == null) {
            return null;
        }

      return switch (s) {
        case "CONTRIBUYENTE" -> fuenteDinamica;
        case "DATASET" -> fuenteEstatica;
        case "PROXY" -> fuenteProxy;
        default -> null;
      };
    }
}
