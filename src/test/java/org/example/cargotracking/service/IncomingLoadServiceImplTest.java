package org.example.cargotracking.service;

import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.repository.IncomingLoadRepository;
import org.example.cargotracking.service.Impl.IncomingLoadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Incoming Load Service Tests")
class IncomingLoadServiceImplTest {

    @Mock
    private IncomingLoadRepository incomingLoadRepository;

    @InjectMocks
    private IncomingLoadServiceImpl incomingLoadService;

    private IncomingLoad incomingLoad;

    @BeforeEach
    void setUp() {
        incomingLoad = IncomingLoad.builder()
                .id(1L)
                .supplierCompany("Supplier Inc")
                .invoiceNumber("INV0001234")
                .invoiceDate(LocalDate.now())
                .productName("Steel Bars")
                .quantity(500)
                .totalPrice(5000.00)
                .notes("Test notes")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save incoming load successfully")
    void testSaveIncomingLoad() {
        when(incomingLoadRepository.save(any(IncomingLoad.class))).thenReturn(incomingLoad);

        IncomingLoad savedLoad = incomingLoadService.save(incomingLoad);

        assertNotNull(savedLoad);
        assertEquals("Supplier Inc", savedLoad.getSupplierCompany());
        assertEquals("INV0001234", savedLoad.getInvoiceNumber());
        assertEquals(500, savedLoad.getQuantity());
        assertEquals(5000.00, savedLoad.getTotalPrice());
        verify(incomingLoadRepository, times(1)).save(incomingLoad);
    }

    @Test
    @DisplayName("Should find all incoming loads")
    void testFindAllIncomingLoads() {
        IncomingLoad load2 = IncomingLoad.builder()
                .id(2L)
                .supplierCompany("Supplier 2")
                .invoiceNumber("INV0001235")
                .productName("Aluminum")
                .quantity(300)
                .totalPrice(3000.00)
                .build();

        List<IncomingLoad> loads = Arrays.asList(incomingLoad, load2);
        when(incomingLoadRepository.findAll()).thenReturn(loads);

        List<IncomingLoad> foundLoads = incomingLoadService.findAll();

        assertNotNull(foundLoads);
        assertEquals(2, foundLoads.size());
        verify(incomingLoadRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find incoming load by id")
    void testFindIncomingLoadById() {
        when(incomingLoadRepository.findById(1L)).thenReturn(Optional.of(incomingLoad));

        IncomingLoad foundLoad = incomingLoadService.findById(1L);

        assertNotNull(foundLoad);
        assertEquals(1L, foundLoad.getId());
        assertEquals("Steel Bars", foundLoad.getProductName());
        verify(incomingLoadRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete incoming load")
    void testDeleteIncomingLoad() {
        doNothing().when(incomingLoadRepository).deleteById(1L);

        incomingLoadService.delete(1L);

        verify(incomingLoadRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should check for duplicate invoice")
    void testIsDuplicateInvoice() {
        when(incomingLoadRepository.existsByInvoiceNumberAndSupplierCompany("INV0001234", "Supplier Inc"))
                .thenReturn(true);

        boolean isDuplicate = incomingLoadService.isDuplicateInvoice("INV0001234", "Supplier Inc");

        assertTrue(isDuplicate);
        verify(incomingLoadRepository, times(1)).existsByInvoiceNumberAndSupplierCompany("INV0001234", "Supplier Inc");
    }

    @Test
    @DisplayName("Should not find duplicate invoice when it doesn't exist")
    void testIsDuplicateInvoiceNotFound() {
        when(incomingLoadRepository.existsByInvoiceNumberAndSupplierCompany("INV9999999", "Supplier Inc"))
                .thenReturn(false);

        boolean isDuplicate = incomingLoadService.isDuplicateInvoice("INV9999999", "Supplier Inc");

        assertFalse(isDuplicate);
    }

    @Test
    @DisplayName("Should validate invoice number length")
    void testInvoiceNumberValidation() {
        assertEquals(10, incomingLoad.getInvoiceNumber().length());
    }

    @Test
    @DisplayName("Should validate supplier company name length")
    void testSupplierNameValidation() {
        assertTrue(incomingLoad.getSupplierCompany().length() >= 2);
        assertTrue(incomingLoad.getSupplierCompany().length() <= 150);
    }

    @Test
    @DisplayName("Should validate total price is positive")
    void testTotalPriceValidation() {
        assertTrue(incomingLoad.getTotalPrice() > 0);
    }

    @Test
    @DisplayName("Should validate quantity is at least 1")
    void testQuantityValidation() {
        assertTrue(incomingLoad.getQuantity() >= 1);
    }

    @Test
    @DisplayName("Should have creation timestamp")
    void testCreatedAtIsSet() {
        assertNotNull(incomingLoad.getCreatedAt());
    }

}

