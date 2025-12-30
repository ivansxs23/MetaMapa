package ar.edu.utn.frba.dds.webclient.validations;

import ar.edu.utn.frba.dds.webclient.dto.UserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDTO> {

  @Override
  public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {
    if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
      return true; // Se validará @NotBlank aparte
    }
    boolean match = dto.getPassword().equals(dto.getConfirmPassword());
    if (!match) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden")
          .addPropertyNode("confirmPassword") // marca el campo correspondiente
          .addConstraintViolation();
    }
    return match;
  }
}
