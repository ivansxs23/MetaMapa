package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO.PaginatedResponseDTO;
import ar.edu.utn.frba.dds.dto.output.PaginatedDTO;
import ar.edu.utn.frba.dds.entities.Hecho;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RespuestaFactory {
  public PaginatedDTO armarRespuesta(
      PaginatedResponseDTO pagina,
      List<Hecho> hechos,
      Pageable pageable
  ) {
    PaginatedDTO resp = new PaginatedDTO();

    resp.setCurrent_page(pagina.getCurrent_page());
    resp.setPer_page(pagina.getPer_page());
    resp.setPath(pagina.getPath());
    resp.setFirst_page_url(pagina.getFirst_page_url());
    resp.setLast_page_url(pagina.getLast_page_url());
    resp.setNext_page_url(pagina.getNext_page_url());
    resp.setPrev_page_url(pagina.getPrev_page_url());
    resp.setLast_page(pagina.getLast_page());

    resp.setData(hechos);

    int pageNumber = pageable.getPageNumber(); // 0-based
    int pageSize = pageable.getPageSize();

    Integer from = hechos.isEmpty()
        ? null
        : (pageNumber * pageSize) + 1;

    Integer to = hechos.isEmpty()
        ? null
        : (pageNumber * pageSize) + hechos.size();

    resp.setFrom(from);
    resp.setTo(to);

    resp.setTotal(hechos.size());

    return resp;
  }

}
