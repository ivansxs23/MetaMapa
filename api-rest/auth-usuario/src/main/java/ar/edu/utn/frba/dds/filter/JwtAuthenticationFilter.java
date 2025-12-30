package ar.edu.utn.frba.dds.filter;

import ar.edu.utn.frba.dds.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
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
      } else {
        System.out.println("No hay token de autorización");
      }
      filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    System.out.println("URI={}" + request.getRequestURI() + "-" + "ctx={}" + request.getContextPath()+"-"+ "servletPath={}" +
           request.getServletPath());
    return PUBLIC_ENDPOINTS.stream().anyMatch(m -> m.matches(request));
  }
  private static final List<RequestMatcher> PUBLIC_ENDPOINTS = List.of(
      new AntPathRequestMatcher("/auth/login/**"),
      new AntPathRequestMatcher("/auth/signup/**"),
      new AntPathRequestMatcher("/auth/refresh/")
  );
}
