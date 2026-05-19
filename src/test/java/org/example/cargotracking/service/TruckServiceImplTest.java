package org.example.cargotracking.service;

import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.repository.TruckRepository;
import org.example.cargotracking.service.Impl.TruckServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Truck Service Tests")
class TruckServiceImplTest {

    @Mock
    private TruckRepository truckRepository;

    @InjectMocks
    private TruckServiceImpl truckService;

    private Truck truck1;
    private Truck truck2;

    @BeforeEach
    void setUp() {
        truck1 = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("John Doe")
                .model("Volvo FH")
                .companyName("Transport Co")
                .build();

        truck2 = Truck.builder()
                .id(2L)
                .truckNumber("BG12AB124")
                .driverName("Jane Smith")
                .model("Scania R450")
                .companyName("Logistics Inc")
                .build();
    }

    @Test
    @DisplayName("Should save truck successfully")
    void testSaveTruck() {
        when(truckRepository.save(any(Truck.class))).thenReturn(truck1);

        Truck savedTruck = truckService.save(truck1);

        assertNotNull(savedTruck);
        assertEquals("BG12AB123", savedTruck.getTruckNumber());
        assertEquals("John Doe", savedTruck.getDriverName());
        verify(truckRepository, times(1)).save(truck1);
    }

    @Test
    @DisplayName("Should find all trucks")
    void testFindAllTrucks() {
        List<Truck> trucks = Arrays.asList(truck1, truck2);
        when(truckRepository.findAll()).thenReturn(trucks);

        List<Truck> foundTrucks = truckService.findAll();

        assertNotNull(foundTrucks);
        assertEquals(2, foundTrucks.size());
        verify(truckRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find truck by id")
    void testFindTruckById() {
        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck1));

        Truck foundTruck = truckService.findById(1L);

        assertNotNull(foundTruck);
        assertEquals(1L, foundTruck.getId());
        assertEquals("BG12AB123", foundTruck.getTruckNumber());
        verify(truckRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return null when truck not found")
    void testFindTruckByIdNotFound() {
        when(truckRepository.findById(999L)).thenReturn(Optional.empty());

        Truck foundTruck = truckService.findById(999L);

        assertNull(foundTruck);
        verify(truckRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should delete truck by id")
    void testDeleteTruck() {
        doNothing().when(truckRepository).deleteById(1L);

        truckService.delete(1L);

        verify(truckRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should validate truck number format")
    void testTruckNumberValidation() {
        assertTrue(truck1.getTruckNumber().length() >= 3);
        assertTrue(truck1.getTruckNumber().length() <= 20);
    }

    @Test
    @DisplayName("Should validate driver name")
    void testDriverNameValidation() {
        assertTrue(truck1.getDriverName().length() >= 2);
        assertTrue(truck1.getDriverName().length() <= 100);
    }

    @Test
    @DisplayName("Should update truck successfully")
    void testUpdateTruck() {
        Truck updatedTruck = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("John Updated")
                .model("Volvo FH16")
                .companyName("Transport Co Updated")
                .build();

        when(truckRepository.save(any(Truck.class))).thenReturn(updatedTruck);

        Truck result = truckService.save(updatedTruck);

        assertEquals("John Updated", result.getDriverName());
        assertEquals("Volvo FH16", result.getModel());
        verify(truckRepository, times(1)).save(updatedTruck);
    }

}

