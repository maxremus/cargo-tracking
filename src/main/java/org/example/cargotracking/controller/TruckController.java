package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.service.TruckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trucks")
public class TruckController {

    private final TruckService truckService;

    @GetMapping
    public String trucks(Model model, HttpServletRequest request) {
        var trucks = truckService.findAll();
        model.addAttribute("trucks", trucks);
        model.addAttribute("truck", new Truck());
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("totalLoads", 0);
        model.addAttribute("currentPath", request.getServletPath());
        return "trucks";
    }

    @GetMapping("/edit/{id}")
    public String editTruck(@PathVariable Long id, Model model, HttpServletRequest request) {
        var trucks = truckService.findAll();
        model.addAttribute("trucks", trucks);
        model.addAttribute("truck", truckService.findById(id));
        model.addAttribute("totalTrucks", trucks.size());
        model.addAttribute("totalLoads", 0);
        model.addAttribute("currentPath", request.getServletPath());
        return "trucks";
    }

    @PostMapping("/save")
    public String saveTruck(@ModelAttribute Truck truck) {

        truckService.save(truck);

        return "redirect:/trucks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTruck(@PathVariable Long id) {
        truckService.delete(id);
        return "redirect:/trucks";
    }
}
