package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    System.out.println("=== CONFIGURANDO SECURITY ===");

    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/api/colecciones/**","api/hechos/**", "api/categorias", "/auth/refresh", "/auth/login", "api/estadisticas","api/solicitudes/**", "api/consensuar").permitAll();
          auth.requestMatchers(
              "/v3/api-docs/**",
              "/swagger-ui.html",
              "/swagger-ui/**"
          ).permitAll();

            // 3. --- NUEVO: OBSERVABILIDAD (ACTUATOR) ---
            // Esto permite que Prometheus lea métricas y Docker haga Health Checks
            auth.requestMatchers("/actuator/**").permitAll();
            auth.requestMatchers("/api/test/**").permitAll();

          auth.anyRequest().authenticated();
        })
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
