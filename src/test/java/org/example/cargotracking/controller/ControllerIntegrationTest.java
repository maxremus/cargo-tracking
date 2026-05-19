package org.example.cargotracking.controller;

import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.TruckService;
import org.example.cargotracking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integration Tests - Controller Layer")
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TruckService truckService;

    @Autowired
    private LoadRecordService loadRecordService;

    @BeforeEach
    void setUp() {
        // Create test user
        User user = User.builder()
                .username("testworker")
                .password("password123")
                .role(UserRole.WORKER)
                .enabled(true)
                .build();
        userService.save(user);

        // Create test truck
        Truck truck = Truck.builder()
                .truckNumber("CTRL123")
                .driverName("Controller Test Driver")
                .model("Volvo FH")
                .build();
        truckService.save(truck);

        // Create test load
        LoadRecord load = LoadRecord.builder()
                .productName("Test Product")
                .quantity(100)
                .loadedBy("Loader")
                .transportedBy("Driver")
                .truck(truck)
                .status(LoadStatus.PENDING)
                .createdBy(user)
                .build();
        loadRecordService.save(load);
    }

    @Test
    @DisplayName("Should access login page without authentication")
    void testAccessLoginPageWithoutAuth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should redirect to login when accessing dashboard without auth")
    void testAccessDashboardWithoutAuth() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should access dashboard with authentication")
    void testAccessDashboardWithAuth() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should access loads page with WORKER role")
    void testAccessLoadsPageWithWorkerRole() throws Exception {
        mockMvc.perform(get("/loads"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should access trucks page with WORKER role")
    void testAccessTrucksPageWithWorkerRole() throws Exception {
        mockMvc.perform(get("/trucks"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should access incoming loads page with WORKER role")
    void testAccessIncomingLoadsPageWithWorkerRole() throws Exception {
        mockMvc.perform(get("/incoming-loads"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin", roles = "ADMIN")
    @DisplayName("Should access users page with ADMIN role")
    void testAccessUsersPageWithAdminRole() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should deny access to users page for WORKER role")
    void testDenyAccessUsersPageForWorkerRole() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadmin", roles = "ADMIN")
    @DisplayName("Should access reports page with ADMIN role")
    void testAccessReportsPageWithAdminRole() throws Exception {
        mockMvc.perform(get("/admin/reports"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testworker", roles = "WORKER")
    @DisplayName("Should deny access to reports page for WORKER role")
    void testDenyAccessReportsPageForWorkerRole() throws Exception {
        mockMvc.perform(get("/admin/reports"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should access home page without authentication")
    void testAccessHomePageWithoutAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}


