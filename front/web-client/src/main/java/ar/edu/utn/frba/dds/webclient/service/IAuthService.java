package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.LoginRequestDTO;
import ar.edu.utn.frba.dds.webclient.dto.RolesPermisosDTO;
import ar.edu.utn.frba.dds.webclient.dto.UserDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.Auth.AuthResponseDTO;

public interface IAuthService {
  AuthResponseDTO login(LoginRequestDTO request);
  void signUp(UserDTO request);
  RolesPermisosDTO getRolesPermisos(String accessToken);
}
