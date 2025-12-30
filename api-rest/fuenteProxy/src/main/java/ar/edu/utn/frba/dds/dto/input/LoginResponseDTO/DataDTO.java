package ar.edu.utn.frba.dds.dto.input.LoginResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDTO {
  private String access_token;
  private String token_type;
  private UserDTO user;
}
