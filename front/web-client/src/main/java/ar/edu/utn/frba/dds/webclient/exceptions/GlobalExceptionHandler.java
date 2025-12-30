package ar.edu.utn.frba.dds.webclient.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("Error inesperado: ", ex);  // Importante: registrar en logs

        model.addAttribute("titulo", "Error interno del servidor");
        model.addAttribute("mensaje", "Ha ocurrido un problema inesperado. Por favor, inténtalo más tarde.");

        return "error/500";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("titulo", "No encontrado");
        model.addAttribute("mensaje", "La pagina o recurso que buscas no existe.");
        return "error/404";
    }
}