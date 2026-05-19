package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.repository.UserRepository;
import org.example.cargotracking.service.SecurityService;
import org.example.cargotracking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @Override
    public User save(User user) {

        // CREATE

        if (user.getId() == null) {

            if (userRepository
                    .findByUsernameAndCompany(
                            user.getUsername(),
                            securityService.getCurrentCompany()
                    )
                    .isPresent()) {

                throw new RuntimeException(
                        "Username already exists"
                );
            }

            user.setPassword(
                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );

            // SET COMPANY

            user.setCompany(
                    securityService.getCurrentCompany()
            );

            return userRepository.save(user);
        }

        // UPDATE

        User existingUser =
                userRepository
                        .findByIdAndCompany(
                                user.getId(),
                                securityService.getCurrentCompany()
                        )
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "User not found"
                                )
                        );

        existingUser.setUsername(
                user.getUsername()
        );

        existingUser.setRole(
                user.getRole()
        );

        existingUser.setEnabled(
                user.isEnabled()
        );

        // PASSWORD CHANGE ONLY IF FILLED

        if (user.getPassword() != null
                &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(

                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );
        }

        return userRepository.save(
                existingUser
        );
    }

    @Override
    public User findByUsername(
            String username
    ) {

        return userRepository
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAllByCompany(
                securityService.getCurrentCompany()
        );
    }

    @Override
    public void delete(Long id) {

        User user =
                userRepository
                        .findByIdAndCompany(
                                id,
                                securityService.getCurrentCompany()
                        )
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "User not found"
                                )
                        );

        // SOFT DELETE

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {

        return userRepository
                .findByIdAndCompany(
                        id,
                        securityService.getCurrentCompany()
                )
                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}
