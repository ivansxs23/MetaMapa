package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.filtro.FiltroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface IHechoService {

  Page<Hecho> obtenerHechos(FiltroDTO filtro, Pageable pageable);
  void importarHechosPorLote(MultipartFile archivo);

}
