package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.service.SystemLogService;
import org.example.cargotracking.service.TruckService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trucks")
public class TruckController {

    private final TruckService truckService;
    private final SystemLogService systemLogService;

    @GetMapping
    public String trucks(
            Model model,
            HttpServletRequest request
    ) {

        var trucks =
                truckService.findAll();

        model.addAttribute(
                "trucks",
                trucks
        );

        model.addAttribute(
                "truck",
                new Truck()
        );

        model.addAttribute(
                "totalTrucks",
                trucks.size()
        );

        model.addAttribute(
                "totalLoads",
                0
        );

        return "trucks";
    }

    @GetMapping("/edit/{id}")
    public String editTruck(

            @PathVariable Long id,

            Model model,

            HttpServletRequest request

    ) {

        var trucks =
                truckService.findAll();

        model.addAttribute(
                "trucks",
                trucks
        );

        model.addAttribute(
                "truck",
                truckService.findById(id)
        );

        model.addAttribute(
                "totalTrucks",
                trucks.size()
        );

        model.addAttribute(
                "totalLoads",
                0
        );

        return "trucks";
    }

    @PostMapping("/save")
    public String saveTruck(

            @Valid
            @ModelAttribute("truck")
            Truck truck,

            BindingResult bindingResult,

            Model model,

            HttpServletRequest request

    ) {

        if (bindingResult.hasErrors()) {

            var trucks =
                    truckService.findAll();

            model.addAttribute(
                    "trucks",
                    trucks
            );

            model.addAttribute(
                    "totalTrucks",
                    trucks.size()
            );

            model.addAttribute(
                    "totalLoads",
                    0
            );

            return "trucks";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isNew =
                truck.getId() == null;

        truckService.save(truck);

        if (isNew) {

            systemLogService.log(

                    auth.getName(),

                    "CREATE_TRUCK",

                    "Добавен камион: "
                            + truck.getTruckNumber(),

                    request.getRemoteAddr()
            );

        } else {

            systemLogService.log(

                    auth.getName(),

                    "UPDATE_TRUCK",

                    "Редактиран камион: "
                            + truck.getTruckNumber(),

                    request.getRemoteAddr()
            );
        }

        return "redirect:/trucks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTruck(

            @PathVariable Long id,

            HttpServletRequest request

    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Truck truck =
                truckService.findById(id);

        truckService.delete(id);

        systemLogService.log(

                auth.getName(),

                "DELETE_TRUCK",

                "Изтрит камион: "
                        + truck.getTruckNumber(),

                request.getRemoteAddr()
        );

        return "redirect:/trucks";
    }
}