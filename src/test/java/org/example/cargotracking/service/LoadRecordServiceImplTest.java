package org.example.cargotracking.service;

import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.service.Impl.LoadRecordServiceImpl;
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
@DisplayName("Load Record Service Tests")
class LoadRecordServiceImplTest {

    @Mock
    private LoadRecordRepository loadRecordRepository;

    @InjectMocks
    private LoadRecordServiceImpl loadRecordService;

    private LoadRecord loadRecord;
    private Truck truck;
    private User user;

    @BeforeEach
    void setUp() {
        truck = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("John Doe")
                .build();

        user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        loadRecord = LoadRecord.builder()
                .id(1L)
                .productName("Cement")
                .quantity(100)
                .loadedBy("John")
                .transportedBy("Mike")
                .truck(truck)
                .loadingDate(LocalDateTime.now())
                .status(LoadStatus.PENDING)
                .notes("Test notes")
                .createdBy(user)
                .build();
    }

    @Test
    @DisplayName("Should save load record successfully")
    void testSaveLoadRecord() {
        when(loadRecordRepository.save(any(LoadRecord.class))).thenReturn(loadRecord);

        LoadRecord savedLoad = loadRecordService.save(loadRecord);

        assertNotNull(savedLoad);
        assertEquals("Cement", savedLoad.getProductName());
        assertEquals(100, savedLoad.getQuantity());
        assertEquals(LoadStatus.PENDING, savedLoad.getStatus());
        verify(loadRecordRepository, times(1)).save(loadRecord);
    }

    @Test
    @DisplayName("Should find all load records")
    void testFindAllLoadRecords() {
        LoadRecord loadRecord2 = LoadRecord.builder()
                .id(2L)
                .productName("Steel")
                .quantity(50)
                .status(LoadStatus.LOADING)
                .truck(truck)
                .build();

        List<LoadRecord> records = Arrays.asList(loadRecord, loadRecord2);
        when(loadRecordRepository.findAll()).thenReturn(records);

        List<LoadRecord> foundRecords = loadRecordService.findAll();

        assertNotNull(foundRecords);
        assertEquals(2, foundRecords.size());
        verify(loadRecordRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find load record by id")
    void testFindLoadRecordById() {
        when(loadRecordRepository.findById(1L)).thenReturn(Optional.of(loadRecord));

        LoadRecord foundRecord = loadRecordService.findById(1L);

        assertNotNull(foundRecord);
        assertEquals(1L, foundRecord.getId());
        assertEquals("Cement", foundRecord.getProductName());
        verify(loadRecordRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete load record")
    void testDeleteLoadRecord() {
        doNothing().when(loadRecordRepository).deleteById(1L);

        loadRecordService.delete(1L);

        verify(loadRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should find loads by status PENDING")
    void testFindLoadsByStatusPending() {
        List<LoadRecord> pendingLoads = Arrays.asList(loadRecord);
        when(loadRecordRepository.findByStatus(LoadStatus.PENDING)).thenReturn(pendingLoads);

        List<LoadRecord> foundLoads = loadRecordService.findByStatus(LoadStatus.PENDING);

        assertNotNull(foundLoads);
        assertEquals(1, foundLoads.size());
        assertEquals(LoadStatus.PENDING, foundLoads.get(0).getStatus());
        verify(loadRecordRepository, times(1)).findByStatus(LoadStatus.PENDING);
    }

    @Test
    @DisplayName("Should find loads by status DELIVERED")
    void testFindLoadsByStatusDelivered() {
        LoadRecord deliveredLoad = LoadRecord.builder()
                .id(2L)
                .productName("Steel")
                .status(LoadStatus.DELIVERED)
                .truck(truck)
                .build();

        List<LoadRecord> deliveredLoads = Arrays.asList(deliveredLoad);
        when(loadRecordRepository.findByStatus(LoadStatus.DELIVERED)).thenReturn(deliveredLoads);

        List<LoadRecord> foundLoads = loadRecordService.findByStatus(LoadStatus.DELIVERED);

        assertEquals(1, foundLoads.size());
        assertEquals(LoadStatus.DELIVERED, foundLoads.get(0).getStatus());
    }

    @Test
    @DisplayName("Should find loads by product name")
    void testFindLoadsByProductName() {
        List<LoadRecord> loads = Arrays.asList(loadRecord);
        when(loadRecordRepository.findByProductNameContaining("Cement")).thenReturn(loads);

        List<LoadRecord> foundLoads = loadRecordService.findByProductName("Cement");

        assertNotNull(foundLoads);
        assertEquals(1, foundLoads.size());
        assertEquals("Cement", foundLoads.get(0).getProductName());
        verify(loadRecordRepository, times(1)).findByProductNameContaining("Cement");
    }

    @Test
    @DisplayName("Should validate quantity is positive")
    void testQuantityValidation() {
        assertTrue(loadRecord.getQuantity() > 0);
        assertTrue(loadRecord.getQuantity() <= 1000000);
    }

    @Test
    @DisplayName("Should validate product name length")
    void testProductNameValidation() {
        assertTrue(loadRecord.getProductName().length() >= 2);
        assertTrue(loadRecord.getProductName().length() <= 100);
    }

    @Test
    @DisplayName("Should have loading date set")
    void testLoadingDateIsSet() {
        assertNotNull(loadRecord.getLoadingDate());
        assertFalse(loadRecord.getLoadingDate().isAfter(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should track who created the load")
    void testCreatedByTracking() {
        assertNotNull(loadRecord.getCreatedBy());
        assertEquals("testuser", loadRecord.getCreatedBy().getUsername());
    }

}

