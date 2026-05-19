package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.CompanySettings;
import org.example.cargotracking.repository.CompanySettingsRepository;
import org.example.cargotracking.repository.UserRepository;
import org.example.cargotracking.service.CompanySettingsService;
import org.example.cargotracking.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanySettingsServiceImpl implements CompanySettingsService {

    private final CompanySettingsRepository companySettingsRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    @Override
    public CompanySettings getSettings() {
        Company company = securityService.getCurrentCompany();
        if (company == null) {
            throw new RuntimeException("No company found for current user");
        }

        return companySettingsRepository.findByCompany(company).orElseGet(() -> {
            CompanySettings settings = CompanySettings.builder()
                    .company(company)
                    .companyName(company.getName())
                    .language("bg")
                    .timezone("Europe/Sofia")
                    .build();
            return companySettingsRepository.save(settings);
        });
    }

    public Long getCurrentCompanyId() {
        Company company = securityService.getCurrentCompany();
        return company != null ? company.getId() : null;
    }

    @Override
    public CompanySettings save(CompanySettings settings, MultipartFile logo) throws Exception {
        Company company = securityService.getCurrentCompany();
        CompanySettings existing = getSettings();
        existing.setCompanyName(settings.getCompanyName());
        existing.setEik(settings.getEik());
        existing.setAddress(settings.getAddress());
        existing.setPhone(settings.getPhone());
        existing.setEmail(settings.getEmail());
        existing.setSmtpHost(settings.getSmtpHost());
        existing.setSmtpPort(settings.getSmtpPort());
        existing.setSmtpUsername(settings.getSmtpUsername());
        existing.setSmtpPassword(settings.getSmtpPassword());
        existing.setSmtpTls(settings.isSmtpTls());
        existing.setNotifyNewLoad(settings.isNotifyNewLoad());
        existing.setNotifyIncomingLoad(settings.isNotifyIncomingLoad());
        existing.setDailyReports(settings.isDailyReports());
        existing.setDarkMode(settings.isDarkMode());
        existing.setLanguage(settings.getLanguage());
        existing.setTimezone(settings.getTimezone());
        existing.setCompany(company);

        if (logo != null && !logo.isEmpty()) {
            String originalFilename = logo.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + "_logo" + extension;
            Path uploadPath = Paths.get("uploads", "logos", fileName);
            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, logo.getBytes());
            existing.setLogo(fileName);
        }

        return companySettingsRepository.save(existing);
    }

    @Override
    public Long getCompanyIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username))
                .getCompany()
                .getId();
    }

    @Override
    public CompanySettings getSettingsByCompanyId(Long companyId) {
        return companySettingsRepository
                .findByCompany_Id(companyId)
                .orElse(null);
    }
}