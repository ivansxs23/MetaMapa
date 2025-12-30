package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.models.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Getter
    private final Key key;

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 minutos
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(decodedKey);
    }


    public String generarAccessToken(Usuario usuario) {
        return Jwts.builder()
            .setSubject(usuario.getUsername())
            .setIssuer("auth-server")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
            .claim("role", usuario.getRol().name())
            .claim("permissions", usuario.getPermisos().stream()
                .map(Enum::name)
                .toList())
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generarRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("auth-server")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean isTokenValid(String token, String usernameDB) {
        final String usernameToken = getUsernameFromToken(token);
        return (usernameToken.equals(usernameDB) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public List<String> getRoleAndPermissions(String token) {
        List<String> authorities = new ArrayList<>();

        authorities.add("ROLE_" + getRole(token)) ;

        List<String> permissions = getPermissions(token).stream()
            .map(p -> "CAN_" + p.toString())
            .toList();
        authorities.addAll(permissions);
        return authorities;
    }
    public String getRole(String token){
        Claims claims = getAllClaims(token);
        return claims.get("role", String.class);
    }
    public List<?> getPermissions(String token){
        Claims claims = getAllClaims(token);
        return claims.get("permissions", List.class);
    }
}
