package com.example.shoppingonlybackend.controller;

import com.example.shoppingonlybackend.entity.User;
import com.example.shoppingonlybackend.payload.LoginDTO;
import com.example.shoppingonlybackend.payload.LoginRequest;
import com.example.shoppingonlybackend.service.UserService;
import com.example.shoppingonlybackend.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {

    final JwtUtil jwtUtil;
    final UserService userService;

    /**
     * L1: Login işlemlerini gerçekleştirir.
     *
     * @param request Login ayrıntıları
     * @return Token değeri
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {

            User user = userService.getByUsername(request.getUsername());

            ObjectMapper mapper = new ObjectMapper();

            List<String> permissions = null;
            try {
                if (user.getPermissionsJson() != null)
                    permissions = mapper.readValue(user.getPermissionsJson(), new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException("Error parsing permissions JSON");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            if (permissions != null)
                claims.put("permissions", permissions);

            String token = jwtUtil.generateToken(claims, user.getUsername());

            return ResponseEntity.ok(new LoginDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
