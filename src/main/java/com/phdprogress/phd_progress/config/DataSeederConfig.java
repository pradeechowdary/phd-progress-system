package com.phdprogress.phd_progress.config;

import com.phdprogress.phd_progress.entity.User;
import com.phdprogress.phd_progress.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataSeederConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSeederConfig.class);

    @Bean
    public CommandLineRunner seedDefaultUsers(UserRepository userRepository,
                                              PasswordEncoder passwordEncoder,
                                              @Value("${app.seed.default-users:true}") boolean seedDefaultUsers) {
        return args -> {
            if (!seedDefaultUsers) {
                log.info("Default user seeding disabled");
                return;
            }

            List<SeedUser> users = List.of(
                    new SeedUser("student1", "student1@phdprogress.local", "Student@123", "STUDENT"),
                    new SeedUser("student2", "student2@phdprogress.local", "Student@123", "STUDENT"),
                    new SeedUser("student3", "student3@phdprogress.local", "Student@123", "STUDENT"),
                    new SeedUser("advisor1", "advisor1@phdprogress.local", "Advisor@123", "ADVISOR"),
                    new SeedUser("director1", "director1@phdprogress.local", "Director@123", "DIRECTOR"),
                    new SeedUser("admin1", "admin1@phdprogress.local", "Admin@123", "ADMIN")
            );

            for (SeedUser seedUser : users) {
                if (userRepository.existsByUsername(seedUser.username())) {
                    continue;
                }

                User user = new User();
                user.setUsername(seedUser.username());
                user.setEmail(seedUser.email());
                user.setPassword(passwordEncoder.encode(seedUser.password()));
                user.setRole(seedUser.role());
                userRepository.save(user);

                log.info("Seeded default user username={} role={}", seedUser.username(), seedUser.role());
            }
        };
    }

    private record SeedUser(String username, String email, String password, String role) {
    }
}
