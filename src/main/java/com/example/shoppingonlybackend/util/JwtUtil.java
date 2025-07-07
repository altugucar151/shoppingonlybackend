package com.example.shoppingonlybackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final SecretKey Secret_Key;

    public JwtUtil() {
        String base64Key = "u8J3Z9xhqFvW1HnxlaOiVFtK0K2vJ1pqKZ4J7O9cTcg=";
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        this.Secret_Key = Keys.hmacShaKeyFor(keyBytes);
    }
    //Tokendan username burda çekiliyor!!
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //token'ın bitme süresi burda çekiliyor!!
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //claim burdan çekiliyor!!
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Token süresi doldu mu ona bakıyoruz!!
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token oluşturur
    public String generateToken(Map<String, Object> claims, String subject) {
        long expriationTime = 1000 * 60 * 60 * 24 * 7;
        return Jwts.builder() //yapıcı objesini başlatır
                .setClaims(claims) //claimi kurar
                .setSubject(subject)  //subject yani username kurulur
                .setIssuedAt(new Date(System.currentTimeMillis())) //token oluşturulma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + expriationTime)) //token son kullanma
                .signWith(Secret_Key, SignatureAlgorithm.HS256) //imza güvenlik için
                .compact(); //tüm ayarlanan bilgileri birleştirip oluşturur
    }
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    // Tüm claim'leri çek
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()//analiz etme işlemi
                .setSigningKey(Secret_Key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
