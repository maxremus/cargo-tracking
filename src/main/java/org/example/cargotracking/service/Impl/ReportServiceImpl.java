package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.DailyLoadStatsDTO;
import org.example.cargotracking.dto.LoaderStatsDTO;
import org.example.cargotracking.dto.TruckStatsDTO;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final LoadRecordRepository loadRecordRepository;

    @Override
    public List<LoaderStatsDTO> getLoaderStats() {
        return loadRecordRepository.getLoaderStats();
    }

    @Override
    public List<TruckStatsDTO> getTruckStats() {
        return loadRecordRepository.getTruckStats();
    }

    @Override
    public List<DailyLoadStatsDTO> getDailyStats() {
        return loadRecordRepository.getDailyStats();
    }
}
