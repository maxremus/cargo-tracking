package org.example.cargotracking.security;

import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Security Integration Tests")
class SecurityIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = User.builder()
                .username("securitytest")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.WORKER)
                .enabled(true)
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Should encode password correctly")
    void testPasswordEncoding() {
        String rawPassword = "password123";
        String encodedPassword = testUser.getPassword();

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    @DisplayName("Should verify password match")
    void testPasswordMatch() {
        String correctPassword = "password123";
        String incorrectPassword = "wrongpassword";

        assertTrue(passwordEncoder.matches(correctPassword, testUser.getPassword()));
        assertFalse(passwordEncoder.matches(incorrectPassword, testUser.getPassword()));
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindUserByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("securitytest");

        assertTrue(foundUser.isPresent());
        assertEquals("securitytest", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should verify user is enabled")
    void testUserIsEnabled() {
        assertTrue(testUser.isEnabled());
    }

    @Test
    @DisplayName("Should have correct role")
    void testUserRole() {
        assertEquals(UserRole.WORKER, testUser.getRole());
    }

    @Test
    @DisplayName("Should create admin user with correct role")
    void testCreateAdminUser() {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .enabled(true)
                .build();
        admin = userRepository.save(admin);

        assertEquals(UserRole.ADMIN, admin.getRole());
        assertNotEquals(UserRole.WORKER, admin.getRole());
    }

    @Test
    @DisplayName("Should not find user with incorrect username")
    void testUserNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("Should disable user account")
    void testDisableUser() {
        testUser.setEnabled(false);
        User disabledUser = userRepository.save(testUser);

        assertFalse(disabledUser.isEnabled());
    }

}

