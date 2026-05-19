package org.example.cargotracking.webConfig;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.CompanySettings;
import org.example.cargotracking.service.CompanySettingsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CompanySettingsService companySettingsService;


    @ModelAttribute("companySettings")
    public CompanySettings companySettings(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // ✅ Вземи от session, ако има
        CompanySettings cached = (CompanySettings) session.getAttribute("companySettings");
        if (cached != null) {
            return cached;
        }

        // ✅ Ако няма - зареди и кеширай в session
        try {
            CompanySettings settings = companySettingsService.getSettings();
            session.setAttribute("companySettings", settings);
            return settings;
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ Добави метод за изчистване на кеша при промяна
    @ModelAttribute("currentPath")
    public String currentPath(jakarta.servlet.http.HttpServletRequest request) {
        return request.getServletPath();
    }
}
