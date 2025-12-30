package ar.edu.utn.frba.dds.entities.dto.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> extends PageImpl<T> {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PageResponse(@JsonProperty("content") List<T> content,
                          @JsonProperty("number") int number,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("pageable") JsonNode pageable,
                          @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages,
                          @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("first") boolean first,
                          @JsonProperty("numberOfElements") long numberOfElements) {
    super(content, PageRequest.of(number, size), totalElements);
  }

  public PageResponse() {
    super(new ArrayList<>());
  }
}
