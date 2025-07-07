package com.example.shoppingonlybackend.config;

import com.example.shoppingonlybackend.entity.User;
import com.example.shoppingonlybackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }
            if (userRepository.findByUsername("member") == null) {
                User member = new User();
                member.setUsername("member");
                member.setPassword(passwordEncoder.encode("member123"));
                member.setRole("MEMBER");
                userRepository.save(member);
            }
        };
    }
}
