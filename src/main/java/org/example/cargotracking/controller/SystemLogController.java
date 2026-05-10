package org.example.cargotracking.controller;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/logs")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @GetMapping
    public String logs(Model model) {

        model.addAttribute(
                "logs",
                systemLogService.findAll()
        );

        return "system-logs";
    }
}