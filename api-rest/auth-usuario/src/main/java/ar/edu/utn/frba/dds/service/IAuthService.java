package ar.edu.utn.frba.dds.service;

import ar.edu.utn.frba.dds.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.dtos.LoginRequestDTO;
import ar.edu.utn.frba.dds.dtos.RefreshRequest;
import ar.edu.utn.frba.dds.dtos.SignUpRequestDTO;
import ar.edu.utn.frba.dds.dtos.SignUpResponseDTO;
import ar.edu.utn.frba.dds.dtos.TokenResponse;
import ar.edu.utn.frba.dds.dtos.UserRolesPermissionsDTO;
import ar.edu.utn.frba.dds.models.entities.Usuario;

public interface IAuthService {
    Usuario autenticarUsuario(String username, String password);
    String generarAccessToken(Usuario usuario);
    String generarRefreshToken(String username);
    UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username);
    AuthResponseDTO login(LoginRequestDTO credentials);
    SignUpResponseDTO signUp(SignUpRequestDTO request);
    TokenResponse refresh(RefreshRequest request);

}
