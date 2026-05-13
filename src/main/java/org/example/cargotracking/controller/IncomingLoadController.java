package org.example.cargotracking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.service.IncomingLoadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/incoming-loads")
public class IncomingLoadController {

    private final IncomingLoadService incomingLoadService;

    @GetMapping
    public String incomingLoads(

            @ModelAttribute
            IncomingLoadSearchDTO search,

            Model model

    ) {

        model.addAttribute(
                "incomingLoads",
                incomingLoadService.search(search)
        );

        model.addAttribute(
                "search",
                search
        );

        return "incoming-loads";
    }

    @GetMapping("/create")
    public String createIncomingLoad(
            Model model
    ) {

        model.addAttribute(
                "incomingLoad",
                new IncomingLoad()
        );

        return "create-incoming-load";
    }

    @GetMapping("/edit/{id}")
    public String editIncomingLoad(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "incomingLoad",
                incomingLoadService.findById(id)
        );

        return "edit-incoming-load";
    }

    @PostMapping("/save")
    public String saveIncomingLoad(

            @Valid
            @ModelAttribute("incomingLoad")
            IncomingLoad incomingLoad,

            BindingResult bindingResult

    ) {

        if (bindingResult.hasErrors()) {

            return "create-incoming-load";
        }

        incomingLoadService.save(
                incomingLoad
        );

        return "redirect:/incoming-loads";
    }

    @PostMapping("/update")
    public String updateIncomingLoad(

            @Valid
            @ModelAttribute("incomingLoad")
            IncomingLoad incomingLoad,

            BindingResult bindingResult

    ) {

        if (bindingResult.hasErrors()) {

            return "edit-incoming-load";
        }

        incomingLoadService.save(
                incomingLoad
        );

        return "redirect:/incoming-loads";
    }

    @PostMapping("/delete/{id}")
    public String deleteIncomingLoad(
            @PathVariable Long id
    ) {

        incomingLoadService.delete(id);

        return "redirect:/incoming-loads";
    }
}
