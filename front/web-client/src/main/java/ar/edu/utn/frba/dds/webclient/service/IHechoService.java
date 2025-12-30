package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IHechoService {
    // (Agrega esto a tu interfaz IHechoService también)
    void importarHechosCsv(MultipartFile archivo);

    void crearHecho(HechoDTO hecho, List<MultipartFile> files);
    RestPageResponse<HechoInputDTO> obtenerHechos(FiltroDTO filtro, int page, int size);
     HechoInputDTO obtenerHechoPorId(Long id);
     void editarHecho(Long id, HechoDTO hechoDTO);
}
