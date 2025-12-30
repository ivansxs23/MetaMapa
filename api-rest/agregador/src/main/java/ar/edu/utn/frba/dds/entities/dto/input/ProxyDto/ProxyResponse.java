package ar.edu.utn.frba.dds.entities.dto.input.ProxyDto;
import java.util.List;
import lombok.Data;

@Data
public class ProxyResponse {
  private Integer current_page;
  private List<HechoProxyDto> data;
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
