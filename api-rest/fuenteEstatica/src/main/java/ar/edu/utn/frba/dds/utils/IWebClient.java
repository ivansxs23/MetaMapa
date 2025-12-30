package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.HechoCsvData;
import java.util.List;
import java.util.Map;

public interface IWebClient {
   Map<String, String> obtenerProvinciaPorCoordenada(List<HechoCsvData> lote);
}
