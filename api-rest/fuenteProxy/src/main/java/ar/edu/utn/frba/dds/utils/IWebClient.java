package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.HechoDTO;
import java.util.List;
import java.util.Map;

public interface IWebClient {
   Map<String, String> obtenerProvinciaPorCoordenada(List<HechoDTO> lote);
}
