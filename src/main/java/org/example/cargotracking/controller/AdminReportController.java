package org.example.cargotracking.controller;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.service.IncomingLoadService;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;
    private final LoadRecordService loadRecordService;
    private final IncomingLoadService incomingLoadService;

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String reports(Model model) {

        // REPORT TABLES

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

        // DELIVERED LOADS

        long deliveredLoads =
                loadRecordService.findByStatus(
                        LoadStatus.DELIVERED
                ).size();

        long totalLoads =
                loadRecordService.findAll().size();

        double deliveryPercent =
                totalLoads == 0
                        ? 0
                        : ((double) deliveredLoads / totalLoads) * 100;

        var deliveredLoadsList =
                loadRecordService.findByStatus(
                        LoadStatus.DELIVERED
                );

        String lastDeliveredLoad =
                deliveredLoadsList.isEmpty()
                        ? "Няма"
                        : deliveredLoadsList.get(0)
                        .getProductName();

        model.addAttribute(
                "deliveredLoads",
                deliveredLoads
        );

        model.addAttribute(
                "deliveryPercent",
                Math.round(deliveryPercent)
        );

        model.addAttribute(
                "deliveredLoadsList",
                deliveredLoadsList
        );

        model.addAttribute(
                "lastDeliveredLoad",
                lastDeliveredLoad
        );

        // INCOMING LOADS

        var incomingLoads =
                incomingLoadService.findAll();

        double totalIncomingPrice =
                incomingLoads.stream()
                        .mapToDouble(load ->

                                load.getTotalPrice()
                                        .doubleValue()

                        )
                        .sum();

        String lastInvoiceNumber =
                incomingLoads.isEmpty()
                        ? "Няма"
                        : incomingLoads.get(0)
                        .getInvoiceNumber();

        model.addAttribute(
                "incomingLoadsCount",
                incomingLoads.size()
        );

        model.addAttribute(
                "totalIncomingPrice",
                Math.round(totalIncomingPrice)
        );

        model.addAttribute(
                "lastInvoiceNumber",
                lastInvoiceNumber
        );

        model.addAttribute(
                "incomingLoadsList",
                incomingLoads
        );

        return "reports";
    }
}
