package ar.edu.utn.frba.dds.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String error;
  private String entityName;
  private String message;
  private LocalDateTime timestamp;
}
