package ar.edu.utn.frba.dds.excepciones;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "Not Found",
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        "Conflict",
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RequestAlreadyResolvedException.class)
  public ResponseEntity<ErrorResponse> handleRequestAlreadyResolvedException(RequestAlreadyResolvedException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        "Conflict",
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
