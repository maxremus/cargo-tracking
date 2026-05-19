package org.example.cargotracking.service;

import org.example.cargotracking.entity.CompanySettings;
import org.springframework.web.multipart.MultipartFile;

public interface CompanySettingsService {

    CompanySettings getSettings();

    CompanySettings save(CompanySettings settings, MultipartFile logo) throws Exception;

    Long getCompanyIdByUsername(String username);

    CompanySettings getSettingsByCompanyId(Long companyId);
}
