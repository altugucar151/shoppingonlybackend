    package com.example.shoppingonlybackend.controller;

    import com.example.shoppingonlybackend.entity.User;
    import com.example.shoppingonlybackend.service.UserService;
    import com.example.shoppingonlybackend.util.JwtUtil;
    import com.example.shoppingonlybackend.payload.LoginRequest;
    import com.example.shoppingonlybackend.payload.LoginResponse;

    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationServiceException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;
        private final UserService userService;

        public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
            this.userService = userService;
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest request) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                );

                User user = userService.getByUsername(request.getUsername());

                // permissions JSON string olarak çek (entity'de getter'ı olmalı)
                String permissionsJson = user.getPermissionsJson();

                // JSON string'i listeye çevir (Jackson ObjectMapper lazım)
                ObjectMapper mapper = new ObjectMapper();
                List<String> permissions = mapper.readValue(permissionsJson, new TypeReference<List<String>>() {});

                Map<String, Object> claims = new HashMap<>();
                claims.put("role", user.getRole());
                claims.put("permissions", permissions);

                String token = jwtUtil.generateToken(claims, user.getUsername());

                return ResponseEntity.ok(new LoginResponse(token));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            }
        }

    }
