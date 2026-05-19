package org.example.cargotracking.service;

import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.repository.UserRepository;
import org.example.cargotracking.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("hashedpassword")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();

        adminUser = User.builder()
                .id(2L)
                .username("admin")
                .password("hashedpassword")
                .role(UserRole.ADMIN)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals(UserRole.WORKER, savedUser.getRole());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User foundUser = userService.findByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should return null when user not found by username")
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        User foundUser = userService.findByUsername("nonexistent");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        List<User> users = Arrays.asList(testUser, adminUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by id")
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return null when user not found by id")
    void testFindByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User foundUser = userService.findById(999L);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should find admin users")
    void testFindAdminUsers() {
        List<User> users = Arrays.asList(adminUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.findAll();
        List<User> adminUsers = allUsers.stream()
                .filter(u -> u.getRole() == UserRole.ADMIN)
                .toList();

        assertEquals(1, adminUsers.size());
        assertEquals(UserRole.ADMIN, adminUsers.get(0).getRole());
    }

    @Test
    @DisplayName("Should find enabled users")
    void testFindEnabledUsers() {
        testUser.setEnabled(true);
        adminUser.setEnabled(false);
        List<User> users = Arrays.asList(testUser, adminUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.findAll();
        List<User> enabledUsers = allUsers.stream()
                .filter(User::isEnabled)
                .toList();

        assertEquals(1, enabledUsers.size());
        assertTrue(enabledUsers.get(0).isEnabled());
    }

}

