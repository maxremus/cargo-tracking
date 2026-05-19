package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.SystemLog;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/logs")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @GetMapping
    public String logs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            Model model

    ) {

        Pageable pageable =
                PageRequest.of(

                        page,

                        size,

                        Sort.by("createdAt")
                                .descending()
                );

        Page<SystemLog> logs =
                systemLogService.findAll(
                        pageable
                );

        model.addAttribute(
                "logs",
                logs
        );

        model.addAttribute(
                "currentPage",
                page
        );

        model.addAttribute(
                "totalPages",
                logs.getTotalPages()
        );

        return "system-logs";
    }

    @GetMapping("/export")
    public void exportLogs(
            HttpServletResponse response
    ) throws Exception {

        systemLogService.exportLogs(
                response
        );
    }
}