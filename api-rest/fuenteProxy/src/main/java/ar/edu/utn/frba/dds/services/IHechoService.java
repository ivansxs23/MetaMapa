package ar.edu.utn.frba.dds.services;
import ar.edu.utn.frba.dds.dto.input.FiltroDTO;
import ar.edu.utn.frba.dds.dto.output.PaginatedDTO;
import org.springframework.data.domain.Pageable;

public interface IHechoService {
  PaginatedDTO obtenerHechos(FiltroDTO filtro, Pageable pageable);
}
