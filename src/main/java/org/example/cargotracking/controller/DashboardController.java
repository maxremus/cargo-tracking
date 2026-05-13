package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.service.IncomingLoadService;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.TruckService;
import org.example.cargotracking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final LoadRecordService loadRecordService;
    private final TruckService truckService;
    private final UserService userService;
    private final IncomingLoadService incomingLoadService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        var loads =
                loadRecordService.findAll();

        long pending =
                loads.stream()
                        .filter(l ->
                                l.getStatus() == LoadStatus.PENDING
                        )
                        .count();

        long loading =
                loads.stream()
                        .filter(l ->
                                l.getStatus() == LoadStatus.LOADING
                        )
                        .count();

        long delivered =
                loads.stream()
                        .filter(l ->
                                l.getStatus() == LoadStatus.DELIVERED
                        )
                        .count();

        model.addAttribute(
                "totalLoads",
                loads.size()
        );

        model.addAttribute(
                "totalTrucks",
                truckService.findAll().size()
        );

        model.addAttribute(
                "totalUsers",
                userService.findAll().size()
        );

        model.addAttribute(
                "pendingLoads",
                pending
        );

        model.addAttribute(
                "loadingLoads",
                loading
        );

        model.addAttribute(
                "deliveredLoads",
                delivered
        );

        // INCOMING LOADS

        model.addAttribute(
                "incomingLoadsCount",
                incomingLoadService.findAll().size()
        );

        model.addAttribute(
                "suppliersCount",
                incomingLoadService.countSuppliers()
        );

        model.addAttribute(
                "invoicesCount",
                incomingLoadService.countInvoices()
        );

        return "dashboard";
    }
}
