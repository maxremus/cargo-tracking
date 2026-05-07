package org.example.cargotracking.controller;


import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.TruckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
@RequestMapping("/loads")
public class LoadController {

    private final LoadRecordService loadRecordService;
    private final TruckService truckService;


    @GetMapping
    public String listLoads(Model model, HttpServletRequest request) {
        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();
        model.addAttribute("loads", loads);
        model.addAttribute("totalLoads", loads.size());
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("currentPath", request.getServletPath());
        return "loads";
    }

    @GetMapping("/create")
    public String createLoadForm(Model model, HttpServletRequest request) {
        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();
        model.addAttribute("loadRecord", new LoadRecord());
        model.addAttribute("trucks", trucks);
        model.addAttribute("statuses", LoadStatus.values());
        model.addAttribute("totalLoads", loads.size());
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("currentPath", request.getServletPath());
        return "create-load";
    }

    @GetMapping("/edit/{id}")
    public String editLoadForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();
        LoadRecord load = loadRecordService.findById(id);
        model.addAttribute("loadRecord", load);
        model.addAttribute("trucks", trucks);
        model.addAttribute("statuses", LoadStatus.values());
        model.addAttribute("totalLoads", loads.size());
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("currentPath", request.getServletPath());
        return "edit-load";
    }

    @PostMapping("/save")
    public String saveLoad(
            @ModelAttribute LoadRecord loadRecord,
            @RequestParam("image")
            MultipartFile image
    ) throws Exception {

        loadRecordService.saveLoad(
                loadRecord,
                image
        );

        return "redirect:/loads";
    }

    @PostMapping("/update")
    public String updateLoad(
            @ModelAttribute LoadRecord loadRecord,
            @RequestParam("image")
            MultipartFile image
    ) throws Exception {

        loadRecordService.saveLoad(
                loadRecord,
                image
        );

        return "redirect:/loads";
    }

    @GetMapping("/delete/{id}")
    public String deleteLoad(@PathVariable Long id) {
        loadRecordService.delete(id);
        return "redirect:/loads";
    }
}
