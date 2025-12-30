package ar.edu.utn.frba.dds.dto.input.LoginResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
  private Boolean error;
  private String message;
  private DataDTO data;
}
