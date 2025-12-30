package ar.edu.utn.frba.dds.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {
  private String username;
  private String password;
  private String email;
  private String name;
}
