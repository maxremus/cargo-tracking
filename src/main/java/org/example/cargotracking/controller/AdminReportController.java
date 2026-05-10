package org.example.cargotracking.controller;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String reports(Model model) {

        model.addAttribute(
                "loaderStats",
                reportService.getLoaderStats()
        );

        model.addAttribute(
                "truckStats",
                reportService.getTruckStats()
        );

        model.addAttribute(
                "dailyStats",
                reportService.getDailyStats()
        );

        return "reports";
    }
}
