package com.swapshelf.config;

import com.swapshelf.entity.User;
import com.swapshelf.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Configuration
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // encrypted password
                admin.setRole("ADMIN"); // Should be ROLE_ADMIN in your logic if you're prepending "ROLE_" in SecurityConfig
                userRepository.save(admin);
                System.out.println("✅ Admin user created: username=admin, password=admin123");
            } else {
                System.out.println("✅ Admin user already exists.");
            }
        };
    }
}
