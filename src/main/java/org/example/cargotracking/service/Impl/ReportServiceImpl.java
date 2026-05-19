package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.DailyLoadStatsDTO;
import org.example.cargotracking.dto.LoaderStatsDTO;
import org.example.cargotracking.dto.TruckStatsDTO;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.service.ReportService;
import org.example.cargotracking.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final LoadRecordRepository loadRecordRepository;
    private final SecurityService securityService;

    @Override
    public List<LoaderStatsDTO> getLoaderStats() {
        return loadRecordRepository.getLoaderStats(securityService.getCurrentCompany());
    }

    @Override
    public List<TruckStatsDTO> getTruckStats() {
        return loadRecordRepository.getTruckStats(securityService.getCurrentCompany());
    }

    @Override
    public List<DailyLoadStatsDTO> getDailyStats() {
        return loadRecordRepository.getDailyStats(securityService.getCurrentCompany());
    }
}
