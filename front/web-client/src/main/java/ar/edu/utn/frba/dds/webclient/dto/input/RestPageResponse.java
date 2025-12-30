package ar.edu.utn.frba.dds.webclient.dto.input;

import java.util.List;
import lombok.Data;

@Data
public class RestPageResponse<T> {
  private List<T> content;
  private int number;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean last;
  private boolean first;
}