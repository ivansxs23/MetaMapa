package ar.edu.utn.frba.dds.webclient.dto;

import ar.edu.utn.frba.dds.webclient.validations.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@PasswordMatches
public class UserDTO {
  @NotBlank(message = "El nombre de usuario es obligatorio")
  private String username;

  @NotBlank(message = "El nombre completo es obligatorio")
  private String name;

  @Email(message = "Debe ser un correo válido")
  @NotBlank(message = "El correo es obligatorio")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  @NotBlank(message = "Debe confirmar la contraseña")
  private String confirmPassword;


}
