package ar.edu.utn.frba.dds.dto.input.LoginResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
  private Integer id;
  private String email;
  private String name;
}