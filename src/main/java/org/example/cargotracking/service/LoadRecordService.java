package org.example.cargotracking.service;

import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LoadRecordService {

    LoadRecord save(LoadRecord loadRecord);

    List<LoadRecord> findAll();

    LoadRecord findById(Long id);

    void delete(Long id);

    List<LoadRecord> findByStatus(LoadStatus status);

    LoadRecord saveLoad(LoadRecord loadRecord, MultipartFile image) throws Exception;
}
