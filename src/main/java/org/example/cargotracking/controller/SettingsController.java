package org.example.cargotracking.controller;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.CompanySettings;
import org.example.cargotracking.service.CompanySettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final CompanySettingsService companySettingsService;

    @GetMapping("/admin/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String settings(Model model) {
        model.addAttribute("settings", companySettingsService.getSettings());
        return "settings";
    }

    @PostMapping("/admin/settings/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveSettings(@ModelAttribute CompanySettings settings,
                               @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) throws Exception {
        companySettingsService.save(settings, logoFile);
        return "redirect:/admin/settings";
    }
}
