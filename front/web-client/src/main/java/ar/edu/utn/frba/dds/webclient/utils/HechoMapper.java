package ar.edu.utn.frba.dds.webclient.utils;

import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {
  public  HechoDTO aHechoDTO(HechoInputDTO hechoInputDTO) {
    HechoDTO hechoDTO = new HechoDTO();
    hechoDTO.setTitulo(hechoInputDTO.getTitulo());
    hechoDTO.setDescripcion(hechoInputDTO.getDescripcion());
    hechoDTO.setFechaAcontecimiento(hechoInputDTO.getFechaAcontecimiento().toString());
    hechoDTO.setIdCategoria(hechoInputDTO.getCategoria().getId());
    hechoDTO.setLatitud(hechoInputDTO.getLatitud());
    hechoDTO.setLongitud(hechoInputDTO.getLongitud());
    hechoDTO.setEsAnonimo(hechoInputDTO.getEsAnonimo());
    return  hechoDTO;
  }
}
