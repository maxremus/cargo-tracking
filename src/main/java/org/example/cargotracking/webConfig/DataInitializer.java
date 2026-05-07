package org.example.cargotracking.webConfig;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args)
            throws Exception {

        if (userRepository.findByUsername("admin")
                .isEmpty()) {

            User admin = new User();

            admin.setUsername("admin");

            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );

            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
        }
    }
}
