package ar.edu.utn.frba.dds.webclient.config;


import ar.edu.utn.frba.dds.webclient.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity()
@Configuration
public class SecurityConfig {

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(provider)
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/img/**","/auth/**","/","/error","/404","/uploads/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/hechos/**", "/colecciones/**","solicitudes/crear/{Id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/hechos/crear","hechos/filtrar","/colecciones/{id}/filtrar","/solicitudes").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/", true)
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        )
        .exceptionHandling(ex -> ex

            .authenticationEntryPoint((request, response, authException) ->
                response.sendRedirect("/auth/login")
            )

            .accessDeniedHandler((request, response, accessDeniedException) ->
                response.sendRedirect("/404")
            )

        );
    return http.build();
  }
}
