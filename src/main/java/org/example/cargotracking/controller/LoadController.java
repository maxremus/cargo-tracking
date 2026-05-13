package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.service.ExcelExportService;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.service.SystemLogService;
import org.example.cargotracking.service.TruckService;
import org.example.cargotracking.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loads")
public class LoadController {

    private final LoadRecordService loadRecordService;
    private final TruckService truckService;
    private final UserService userService;
    private final ExcelExportService excelExportService;
    private final SystemLogService systemLogService;

    @GetMapping("/create")
    public String createLoadForm(
            Model model,
            HttpServletRequest request
    ) {

        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();

        model.addAttribute(
                "loadRecord",
                new LoadRecord()
        );

        model.addAttribute(
                "trucks",
                trucks
        );

        model.addAttribute(
                "statuses",
                LoadStatus.values()
        );

        model.addAttribute(
                "totalLoads",
                loads.size()
        );

        model.addAttribute(
                "totalTrucks",
                trucks.size()
        );

        return "create-load";
    }

    @GetMapping("/edit/{id}")
    public String editLoadForm(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request
    ) {

        var loads = loadRecordService.findAll();
        var trucks = truckService.findAll();

        LoadRecord load =
                loadRecordService.findById(id);

        model.addAttribute(
                "loadRecord",
                load
        );

        model.addAttribute(
                "trucks",
                trucks
        );

        model.addAttribute(
                "statuses",
                LoadStatus.values()
        );

        model.addAttribute(
                "totalLoads",
                loads.size()
        );

        model.addAttribute(
                "totalTrucks",
                trucks.size()
        );

        return "edit-load";
    }

    @PostMapping("/save")
    public String saveLoad(

            @Valid
            @ModelAttribute("loadRecord")
            LoadRecord loadRecord,

            BindingResult bindingResult,

            @RequestParam("image")
            MultipartFile image,

            @RequestParam("document")
            MultipartFile document,

            Model model,

            HttpServletRequest request

    ) throws Exception {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "trucks",
                    truckService.findAll()
            );

            model.addAttribute(
                    "statuses",
                    LoadStatus.values()
            );

            return "create-load";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username =
                auth.getName();

        User user =
                userService.findByUsername(username);

        loadRecord.setCreatedBy(user);

        loadRecordService.saveLoad(
                loadRecord,
                image,
                document
        );

        systemLogService.log(

                username,

                "CREATE_LOAD",

                "Добавен товар: "
                        + loadRecord.getProductName(),

                request.getRemoteAddr()
        );

        return "redirect:/loads";
    }

    @PostMapping("/update")
    public String updateLoad(

            @Valid
            @ModelAttribute("loadRecord")
            LoadRecord loadRecord,

            BindingResult bindingResult,

            @RequestParam("image")
            MultipartFile image,

            @RequestParam("document")
            MultipartFile document,

            Model model,

            HttpServletRequest request

    ) throws Exception {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "trucks",
                    truckService.findAll()
            );

            model.addAttribute(
                    "statuses",
                    LoadStatus.values()
            );

            return "edit-load";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        loadRecordService.saveLoad(
                loadRecord,
                image,
                document
        );

        systemLogService.log(

                auth.getName(),

                "UPDATE_LOAD",

                "Редактиран товар ID: "
                        + loadRecord.getId(),

                request.getRemoteAddr()
        );

        return "redirect:/loads";
    }

    @PostMapping("/delete/{id}")
    public String deleteLoad(

            @PathVariable Long id,

            HttpServletRequest request

    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        loadRecordService.delete(id);

        systemLogService.log(

                auth.getName(),

                "DELETE_LOAD",

                "Изтрит товар ID: "
                        + id,

                request.getRemoteAddr()
        );

        return "redirect:/loads";
    }

    @GetMapping
    public String listLoads(

            @ModelAttribute
            LoadSearchDTO search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "loadingDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction,

            Model model

    ) {

        Sort sort =
                direction.equals("asc")
                        ?
                        Sort.by(sortBy).ascending()
                        :
                        Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<LoadRecord> loads =
                loadRecordService.searchLoads(
                        search,
                        pageable
                );

        var trucks =
                truckService.findAll();

        model.addAttribute(
                "loads",
                loads
        );

        model.addAttribute(
                "currentPage",
                page
        );

        model.addAttribute(
                "totalPages",
                loads.getTotalPages()
        );

        model.addAttribute(
                "search",
                search
        );

        model.addAttribute(
                "statuses",
                LoadStatus.values()
        );

        model.addAttribute(
                "totalLoads",
                loads.getTotalElements()
        );

        model.addAttribute(
                "totalTrucks",
                trucks.size()
        );

        model.addAttribute(
                "sortBy",
                sortBy
        );

        model.addAttribute(
                "direction",
                direction
        );

        return "loads";
    }

    @GetMapping("/export/excel")
    public void exportExcel(

            @ModelAttribute LoadSearchDTO search,

            HttpServletResponse response

    ) throws Exception {

        excelExportService
                .exportLoadsToExcel(
                        search,
                        response
                );
    }

    @GetMapping("/save")
    public String redirectSave() {

        return "redirect:/loads/create";
    }

    @GetMapping("/update")
    public String redirectUpdate() {

        return "redirect:/loads";
    }
}