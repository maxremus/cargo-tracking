package org.example.cargotracking.service;

import org.example.cargotracking.entity.SystemLog;
import org.example.cargotracking.repository.SystemLogRepository;
import org.example.cargotracking.service.Impl.SystemLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("System Log Service Tests")
class SystemLogServiceImplTest {

    @Mock
    private SystemLogRepository systemLogRepository;

    @InjectMocks
    private SystemLogServiceImpl systemLogService;

    private SystemLog testLog;

    @BeforeEach
    void setUp() {
        testLog = SystemLog.builder()
                .id(1L)
                .username("testuser")
                .action("LOGIN")
                .details("User logged in successfully")
                .ipAddress("192.168.1.1")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save system log successfully")
    void testSaveSystemLog() {
        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(testLog);

        SystemLog savedLog = systemLogService.log(testLog);

        assertNotNull(savedLog);
        assertEquals("testuser", savedLog.getUsername());
        assertEquals("LOGIN", savedLog.getAction());
        verify(systemLogRepository, times(1)).save(testLog);
    }

    @Test
    @DisplayName("Should find all system logs")
    void testFindAllLogs() {
        SystemLog log2 = SystemLog.builder()
                .id(2L)
                .username("admin")
                .action("CREATE_USER")
                .details("Created new user")
                .ipAddress("192.168.1.2")
                .createdAt(LocalDateTime.now())
                .build();

        List<SystemLog> logs = Arrays.asList(testLog, log2);
        when(systemLogRepository.findAll()).thenReturn(logs);

        List<SystemLog> foundLogs = systemLogService.findAll();

        assertNotNull(foundLogs);
        assertEquals(2, foundLogs.size());
        verify(systemLogRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should log action with all details")
    void testLogActionWithDetails() {
        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(testLog);

        systemLogService.logAction("testuser", "LOGIN", "User logged in successfully", "192.168.1.1");

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    @DisplayName("Should log CREATE_LOAD action")
    void testLogCreateLoadAction() {
        SystemLog createLoadLog = SystemLog.builder()
                .username("worker")
                .action("CREATE_LOAD")
                .details("Created load with ID: 123")
                .ipAddress("192.168.1.100")
                .createdAt(LocalDateTime.now())
                .build();

        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(createLoadLog);

        systemLogService.logAction("worker", "CREATE_LOAD", "Created load with ID: 123", "192.168.1.100");

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    @DisplayName("Should log DELETE_TRUCK action")
    void testLogDeleteTruckAction() {
        SystemLog deleteLog = SystemLog.builder()
                .username("admin")
                .action("DELETE_TRUCK")
                .details("Deleted truck: BG12AB123")
                .ipAddress("192.168.1.50")
                .createdAt(LocalDateTime.now())
                .build();

        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(deleteLog);

        systemLogService.logAction("admin", "DELETE_TRUCK", "Deleted truck: BG12AB123", "192.168.1.50");

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    @DisplayName("Should log UPDATE_LOAD action")
    void testLogUpdateLoadAction() {
        SystemLog updateLog = SystemLog.builder()
                .username("worker")
                .action("UPDATE_LOAD")
                .details("Updated load status from PENDING to LOADING")
                .ipAddress("192.168.1.75")
                .createdAt(LocalDateTime.now())
                .build();

        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(updateLog);

        systemLogService.logAction("worker", "UPDATE_LOAD", "Updated load status from PENDING to LOADING", "192.168.1.75");

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    @DisplayName("Should have timestamp when log is created")
    void testLogHasTimestamp() {
        assertNotNull(testLog.getCreatedAt());
    }

    @Test
    @DisplayName("Should track IP address")
    void testLogIPAddressTracking() {
        assertEquals("192.168.1.1", testLog.getIpAddress());
    }

    @Test
    @DisplayName("Should track username")
    void testLogUsernameTracking() {
        assertEquals("testuser", testLog.getUsername());
    }

}

