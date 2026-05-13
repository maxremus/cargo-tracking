package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.service.IncomingLoadService;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/incoming-loads")
public class IncomingLoadController {

    private final IncomingLoadService incomingLoadService;
    private final SystemLogService systemLogService;

    @GetMapping
    public String incomingLoads(

            @ModelAttribute
            IncomingLoadSearchDTO search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            Model model

    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<IncomingLoad> incomingLoads =
                incomingLoadService.search(
                        search,
                        pageable
                );

        model.addAttribute(
                "incomingLoads",
                incomingLoads
        );

        model.addAttribute(
                "search",
                search
        );

        model.addAttribute(
                "currentPage",
                page
        );

        model.addAttribute(
                "totalPages",
                incomingLoads.getTotalPages()
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
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model

    ) {

        if (bindingResult.hasErrors()) {

            return "create-incoming-load";
        }

        try {

            incomingLoadService.save(
                    incomingLoad
            );

        } catch (RuntimeException e) {

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "create-incoming-load";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        systemLogService.log(

                auth.getName(),

                "CREATE_INCOMING_LOAD",

                "Добавен входящ товар: "
                        + incomingLoad.getProductName(),

                request.getRemoteAddr()
        );

        return "redirect:/incoming-loads";
    }

    @PostMapping("/update")
    public String updateIncomingLoad(

            @Valid
            @ModelAttribute("incomingLoad")
            IncomingLoad incomingLoad,

            BindingResult bindingResult,

            HttpServletRequest request

    ) {

        if (bindingResult.hasErrors()) {

            return "edit-incoming-load";
        }

        incomingLoadService.save(
                incomingLoad
        );

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        systemLogService.log(

                auth.getName(),

                "UPDATE_INCOMING_LOAD",

                "Редактиран входящ товар: "
                        + incomingLoad.getProductName(),

                request.getRemoteAddr()
        );

        return "redirect:/incoming-loads";
    }

    @PostMapping("/delete/{id}")
    public String deleteIncomingLoad(

            @PathVariable Long id,

            HttpServletRequest request

    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        IncomingLoad incomingLoad =
                incomingLoadService.findById(id);

        incomingLoadService.delete(id);

        systemLogService.log(

                auth.getName(),

                "DELETE_INCOMING_LOAD",

                "Изтрит входящ товар: "
                        + incomingLoad.getProductName(),

                request.getRemoteAddr()
        );

        return "redirect:/incoming-loads";
    }
}
