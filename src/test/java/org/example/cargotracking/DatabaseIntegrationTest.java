package org.example.cargotracking;

import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.repository.TruckRepository;
import org.example.cargotracking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Integration Tests - Database Layer")
class DatabaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private LoadRecordRepository loadRecordRepository;

    private User testUser;
    private Truck testTruck;
    private LoadRecord testLoadRecord;

    @BeforeEach
    void setUp() {
        // Clean up data before each test
        loadRecordRepository.deleteAll();
        truckRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = User.builder()
                .username("testuser")
                .password("hashedpassword")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();
        testUser = userRepository.save(testUser);

        // Create test truck
        testTruck = Truck.builder()
                .truckNumber("TEST123")
                .driverName("Test Driver")
                .model("Volvo FH")
                .companyName("Test Company")
                .build();
        testTruck = truckRepository.save(testTruck);

        // Create test load record
        testLoadRecord = LoadRecord.builder()
                .productName("Test Product")
                .quantity(100)
                .loadedBy("Loader")
                .transportedBy("Driver")
                .truck(testTruck)
                .loadingDate(LocalDateTime.now())
                .status(LoadStatus.PENDING)
                .createdBy(testUser)
                .build();
        testLoadRecord = loadRecordRepository.save(testLoadRecord);
    }

    @Test
    @DisplayName("Should save and retrieve user from database")
    void testUserPersistence() {
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals(UserRole.WORKER, foundUser.get().getRole());
    }

    @Test
    @DisplayName("Should save and retrieve truck from database")
    void testTruckPersistence() {
        Optional<Truck> foundTruck = truckRepository.findById(testTruck.getId());

        assertTrue(foundTruck.isPresent());
        assertEquals("TEST123", foundTruck.get().getTruckNumber());
        assertEquals("Test Driver", foundTruck.get().getDriverName());
    }

    @Test
    @DisplayName("Should save and retrieve load record from database")
    void testLoadRecordPersistence() {
        Optional<LoadRecord> foundLoad = loadRecordRepository.findById(testLoadRecord.getId());

        assertTrue(foundLoad.isPresent());
        assertEquals("Test Product", foundLoad.get().getProductName());
        assertEquals(100, foundLoad.get().getQuantity());
        assertEquals(LoadStatus.PENDING, foundLoad.get().getStatus());
    }

    @Test
    @DisplayName("Should find loads by status")
    void testFindLoadsByStatus() {
        List<LoadRecord> pendingLoads = loadRecordRepository.findByStatus(LoadStatus.PENDING);

        assertFalse(pendingLoads.isEmpty());
        assertTrue(pendingLoads.stream().allMatch(l -> l.getStatus() == LoadStatus.PENDING));
    }

    @Test
    @DisplayName("Should find loads by product name")
    void testFindLoadsByProductName() {
        List<LoadRecord> loads = loadRecordRepository.findByProductNameContaining("Test");

        assertFalse(loads.isEmpty());
        assertTrue(loads.stream().allMatch(l -> l.getProductName().contains("Test")));
    }

    @Test
    @DisplayName("Should find loads by truck number")
    void testFindLoadsByTruckNumber() {
        List<LoadRecord> loads = loadRecordRepository.findByTruck_TruckNumber("TEST123");

        assertFalse(loads.isEmpty());
        assertEquals("TEST123", loads.get(0).getTruck().getTruckNumber());
    }

    @Test
    @DisplayName("Should update load record status")
    void testUpdateLoadStatus() {
        testLoadRecord.setStatus(LoadStatus.LOADING);
        LoadRecord updatedLoad = loadRecordRepository.save(testLoadRecord);

        Optional<LoadRecord> retrievedLoad = loadRecordRepository.findById(updatedLoad.getId());

        assertTrue(retrievedLoad.isPresent());
        assertEquals(LoadStatus.LOADING, retrievedLoad.get().getStatus());
    }

    @Test
    @DisplayName("Should delete load record")
    void testDeleteLoadRecord() {
        Long loadId = testLoadRecord.getId();
        loadRecordRepository.deleteById(loadId);

        Optional<LoadRecord> deletedLoad = loadRecordRepository.findById(loadId);

        assertTrue(deletedLoad.isEmpty());
    }

    @Test
    @DisplayName("Should establish relationship between truck and load records")
    void testTruckLoadRecordRelationship() {
        Optional<Truck> truck = truckRepository.findById(testTruck.getId());

        assertTrue(truck.isPresent());
        assertNotNull(testLoadRecord.getTruck());
        assertEquals(truck.get().getId(), testLoadRecord.getTruck().getId());
    }

    @Test
    @DisplayName("Should track created by user in load record")
    void testLoadRecordCreatedByTracking() {
        Optional<LoadRecord> load = loadRecordRepository.findById(testLoadRecord.getId());

        assertTrue(load.isPresent());
        assertNotNull(load.get().getCreatedBy());
        assertEquals(testUser.getId(), load.get().getCreatedBy().getId());
    }

    @Test
    @DisplayName("Should handle multiple loads per truck")
    void testMultipleLoadsPerTruck() {
        LoadRecord load2 = LoadRecord.builder()
                .productName("Product 2")
                .quantity(50)
                .loadedBy("Loader")
                .transportedBy("Driver")
                .truck(testTruck)
                .loadingDate(LocalDateTime.now())
                .status(LoadStatus.LOADING)
                .createdBy(testUser)
                .build();
        loadRecordRepository.save(load2);

        List<LoadRecord> truckLoads = loadRecordRepository.findByTruck_TruckNumber("TEST123");

        assertEquals(2, truckLoads.size());
    }

}



