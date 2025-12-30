package ar.edu.utn.frba.dds.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Base64;

@Getter
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
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
