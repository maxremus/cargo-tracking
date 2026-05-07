package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.TruckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final LoadRecordService loadRecordService;
    private final TruckService truckService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {

        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();

        model.addAttribute("loads", loads);
        model.addAttribute("trucks", trucks);
        model.addAttribute("totalLoads", loads.size());
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("currentPath", request.getServletPath());

        return "dashboard";
    }
}
