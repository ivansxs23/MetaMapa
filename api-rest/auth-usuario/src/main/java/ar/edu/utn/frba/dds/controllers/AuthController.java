package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.LoginRequestDTO;
import ar.edu.utn.frba.dds.dtos.RefreshRequest;
import ar.edu.utn.frba.dds.dtos.SignUpRequestDTO;
import ar.edu.utn.frba.dds.dtos.SignUpResponseDTO;
import ar.edu.utn.frba.dds.dtos.TokenResponse;
import ar.edu.utn.frba.dds.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import ar.edu.utn.frba.dds.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.dtos.UserRolesPermissionsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frba.dds.service.IAuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final IAuthService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO credentials) {
        if (credentials.getUsername() == null || credentials.getUsername().trim().isEmpty() ||
            credentials.getPassword() == null || credentials.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loginService.login(credentials));

    }
    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequest) {
            return ResponseEntity.ok(loginService.signUp(signUpRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(loginService.refresh(request));
    }

    @GetMapping("/user/roles-permisos")
    public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermissions(Authentication authentication) {
        try {
            String username = authentication.getName();
            System.out.println("Se obtuvo el username del token: " + username);
            UserRolesPermissionsDTO response = loginService.obtenerRolesYPermisosUsuario(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

