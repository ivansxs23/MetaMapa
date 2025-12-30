package ar.edu.utn.frba.dds.dto.input.PaginatedResponseDTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginatedResponseDTO {
  private Integer current_page;
  private List<HechoDTO> data;
  private String first_page_url;
  private Integer from;
  private Integer last_page;
  private String last_page_url;
  private String next_page_url;
  private String path;
  private Integer per_page;
  private String prev_page_url;
  private Integer to;
  private Integer total;
}
