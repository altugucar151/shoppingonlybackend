package com.example.shoppingonlybackend.JwtTokenParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtConsumer {

    private final String secret = "u8J3Z9xhqFvW1HnxlaOiVFtK0K2vJ1pqKZ4J7O9cTcg";

    @GetMapping("/api/parse-token")
    public String parseToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);

            Claims claims = parsedToken.getBody();

            StringBuilder sb = new StringBuilder();
            sb.append("Subject: ").append(claims.getSubject()).append("\n");
            sb.append("IssuedAt: ").append(claims.getIssuedAt()).append("\n");
            sb.append("Expiration: ").append(claims.getExpiration()).append("\n");

            claims.forEach((key, value) -> {
                sb.append("Key: ").append(key).append(" - Value: ").append(value).append("\n");
            });

            return sb.toString();

        } catch (Exception e) {
            return "Geçersiz Token: " + e.getMessage();
        }
    }

    @GetMapping("/api/whoami")
    public String whoAmI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return "Giriş yapan kullanıcı: " + username;
        } else {
            return "Principal: " + principal.toString();
        }
    }
}
