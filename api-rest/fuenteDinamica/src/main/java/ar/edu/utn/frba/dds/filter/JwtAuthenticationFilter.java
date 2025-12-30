package ar.edu.utn.frba.dds.filter;


import ar.edu.utn.frba.dds.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header == null  || !header.toLowerCase().startsWith("bearer")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.length() >= 7 ? header.substring(7).trim() : "";
    if (token.isBlank()  || token.equalsIgnoreCase("null")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String username = jwtUtil.validarToken(token);
        var auth = new UsernamePasswordAuthenticationToken(
            username,
            null,
            jwtUtil.getRoleAndPermissions(token).stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (ExpiredJwtException e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        return;
      } catch (MalformedJwtException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token mal formado");
        return;
      } catch (UnsupportedJwtException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo de token no soportado");
        return;
      } catch (IllegalArgumentException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token vacío o inválido");
        return;
      } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error al validar token");
        return;
      }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    return path.equals("/api/login");
  }
}
