package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.dtos.LoginRequestDTO;
import ar.edu.utn.frba.dds.dtos.RefreshRequest;
import ar.edu.utn.frba.dds.dtos.SignUpRequestDTO;
import ar.edu.utn.frba.dds.dtos.SignUpResponseDTO;
import ar.edu.utn.frba.dds.dtos.TokenResponse;
import ar.edu.utn.frba.dds.dtos.UserRolesPermissionsDTO;
import ar.edu.utn.frba.dds.exceptions.EntityAlreadyExistsException;
import ar.edu.utn.frba.dds.exceptions.InvalidTokenTypeException;
import ar.edu.utn.frba.dds.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.models.entities.Permiso;
import ar.edu.utn.frba.dds.models.entities.Rol;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.service.IAuthService;
import ar.edu.utn.frba.dds.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
      this.usuariosRepository = usuariosRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtUtil = jwtUtil;
    }
    public AuthResponseDTO login(LoginRequestDTO credentials) {
            Usuario usuario = autenticarUsuario(credentials.getUsername(), credentials.getPassword());
            System.out.println("Usuario autenticado: " + usuario.getUsername());
            String accessToken = generarAccessToken(usuario);
            String refreshToken = generarRefreshToken(usuario.getUsername());

      return AuthResponseDTO.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
    }

  @Override
  public SignUpResponseDTO signUp(SignUpRequestDTO request) {
      // Verificar si el usuario ya existe
      if (usuariosRepository.findByUsername(request.getUsername()).isPresent()) {
          throw new EntityAlreadyExistsException("username");
      }

      Usuario nuevoUsuario = Usuario.builder()
          .username(request.getUsername())
          .contrasenia(passwordEncoder.encode(request.getPassword()))
          .email(request.getEmail())
          .nombre(request.getName())
          .rol(Rol.USER)
          .permisos(List.of(Permiso.EDITAR_HECHO))
          .build();

      usuariosRepository.save(nuevoUsuario);

      return new SignUpResponseDTO(true);
  }

  @Override
    public TokenResponse refresh(RefreshRequest request) {
            String username = jwtUtil.validarToken(request.getRefreshToken());

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getKey())
                .build()
                .parseClaimsJws(request.getRefreshToken())
                .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                throw new InvalidTokenTypeException("El token proporcionado no es válido.");
            }
            Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));
            String newAccessToken = jwtUtil.generarAccessToken(usuario);

      return new TokenResponse(newAccessToken, request.getRefreshToken());
    }

    @Override
    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByUsername(username);
        
        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            System.out.println("Password ingresada: " + password);
            System.out.println("Password almacenada (hash): " + usuario.getContrasenia());
            System.out.println("Contraseña incorrecta");
            throw new NotFoundException("Usuario", username);
        }
        
        return usuario;
    }

    @Override
    public String generarAccessToken(Usuario usuario) {
        return jwtUtil.generarAccessToken(usuario);
    }

    @Override
    public String generarRefreshToken(String username) {
        return jwtUtil.generarRefreshToken(username);
    }

    @Override
    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByUsername(username);
        
        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }
        
        Usuario usuario = usuarioOpt.get();
        
        return UserRolesPermissionsDTO.builder()
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .permisos(usuario.getPermisos())
                .build();
    }
}
