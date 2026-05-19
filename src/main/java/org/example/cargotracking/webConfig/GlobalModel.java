package org.example.cargotracking.webConfig;

import org.example.cargotracking.service.CompanySettingsService;
import org.springframework.stereotype.Component;

@Component
public class GlobalModel {

    private final CompanySettingsService service;

    public GlobalModel(CompanySettingsService service) {
        this.service = service;
    }

    // This class no longer provides @ModelAttribute methods to avoid duplicate
    // model attribute providers. Use GlobalControllerAdvice for global model attrs.
}
