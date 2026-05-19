package org.example.cargotracking.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Entity Validation Tests")
class EntityValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should validate valid User entity")
    void testValidUserEntity() {
        User user = User.builder()
                .username("validuser")
                .password("password123")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject User with blank username")
    void testUserWithBlankUsername() {
        User user = User.builder()
                .username("")
                .password("password123")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject User with short username")
    void testUserWithShortUsername() {
        User user = User.builder()
                .username("ab")
                .password("password123")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should validate valid Truck entity")
    void testValidTruckEntity() {
        Truck truck = Truck.builder()
                .truckNumber("BG12AB123")
                .driverName("Test Driver")
                .model("Volvo FH")
                .companyName("Test Company")
                .build();

        Set<ConstraintViolation<Truck>> violations = validator.validate(truck);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject Truck with blank truck number")
    void testTruckWithBlankNumber() {
        Truck truck = Truck.builder()
                .truckNumber("")
                .driverName("Test Driver")
                .build();

        Set<ConstraintViolation<Truck>> violations = validator.validate(truck);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject Truck with short truck number")
    void testTruckWithShortNumber() {
        Truck truck = Truck.builder()
                .truckNumber("AB")
                .driverName("Test Driver")
                .build();

        Set<ConstraintViolation<Truck>> violations = validator.validate(truck);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should validate valid LoadRecord entity")
    void testValidLoadRecordEntity() {
        Truck truck = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("Driver")
                .build();

        LoadRecord load = LoadRecord.builder()
                .productName("Test Product")
                .quantity(100)
                .loadedBy("Loader")
                .transportedBy("Transporter")
                .truck(truck)
                .status(LoadStatus.PENDING)
                .build();

        Set<ConstraintViolation<LoadRecord>> violations = validator.validate(load);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject LoadRecord with negative quantity")
    void testLoadRecordWithNegativeQuantity() {
        Truck truck = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("Driver")
                .build();

        LoadRecord load = LoadRecord.builder()
                .productName("Test Product")
                .quantity(-100)
                .loadedBy("Loader")
                .transportedBy("Transporter")
                .truck(truck)
                .status(LoadStatus.PENDING)
                .build();

        Set<ConstraintViolation<LoadRecord>> violations = validator.validate(load);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject LoadRecord with quantity exceeding limit")
    void testLoadRecordWithExcessiveQuantity() {
        Truck truck = Truck.builder()
                .id(1L)
                .truckNumber("BG12AB123")
                .driverName("Driver")
                .build();

        LoadRecord load = LoadRecord.builder()
                .productName("Test Product")
                .quantity(2000000)
                .loadedBy("Loader")
                .transportedBy("Transporter")
                .truck(truck)
                .status(LoadStatus.PENDING)
                .build();

        Set<ConstraintViolation<LoadRecord>> violations = validator.validate(load);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should validate valid IncomingLoad entity")
    void testValidIncomingLoadEntity() {
        IncomingLoad load = IncomingLoad.builder()
                .supplierCompany("Supplier Inc")
                .invoiceNumber("INV0001234")
                .invoiceDate(LocalDate.now())
                .productName("Steel Bars")
                .quantity(500)
                .totalPrice(5000.00)
                .build();

        Set<ConstraintViolation<IncomingLoad>> violations = validator.validate(load);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject IncomingLoad with negative price")
    void testIncomingLoadWithNegativePrice() {
        IncomingLoad load = IncomingLoad.builder()
                .supplierCompany("Supplier Inc")
                .invoiceNumber("INV0001234")
                .invoiceDate(LocalDate.now())
                .productName("Steel Bars")
                .quantity(500)
                .totalPrice(-5000.00)
                .build();

        Set<ConstraintViolation<IncomingLoad>> violations = validator.validate(load);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject IncomingLoad with zero quantity")
    void testIncomingLoadWithZeroQuantity() {
        IncomingLoad load = IncomingLoad.builder()
                .supplierCompany("Supplier Inc")
                .invoiceNumber("INV0001234")
                .invoiceDate(LocalDate.now())
                .productName("Steel Bars")
                .quantity(0)
                .totalPrice(5000.00)
                .build();

        Set<ConstraintViolation<IncomingLoad>> violations = validator.validate(load);

        assertFalse(violations.isEmpty());
    }

}

