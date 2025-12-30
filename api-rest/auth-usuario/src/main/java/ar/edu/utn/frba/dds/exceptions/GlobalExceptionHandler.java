package ar.edu.utn.frba.dds.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "Not Found",
        ex.getMessage(),
        null,
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> EntityAlreadyExistsException(EntityAlreadyExistsException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        "Conflict",
        ex.getEntityName(),
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

}
