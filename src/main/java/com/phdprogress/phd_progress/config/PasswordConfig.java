package com.phdprogress.phd_progress.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String storedPassword) {
                if (storedPassword == null || storedPassword.isBlank()) {
                    return false;
                }

                if (storedPassword.startsWith("$2a$")
                        || storedPassword.startsWith("$2b$")
                        || storedPassword.startsWith("$2y$")) {
                    return bcrypt.matches(rawPassword, storedPassword);
                }

                // Allow legacy plaintext rows already present in PostgreSQL.
                return storedPassword.contentEquals(rawPassword);
            }
        };
    }

}
