package org.example.cargotracking.service;

import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LoadRecordService {


    List<LoadRecord> findAll();

    LoadRecord findById(Long id);

    void delete(Long id);

    List<LoadRecord> findByStatus(LoadStatus status);

    void saveLoad(LoadRecord loadRecord,
                  MultipartFile image,
                  MultipartFile document) throws Exception;

    Page<LoadRecord> searchLoads(
            LoadSearchDTO search,
            Pageable pageable
    );
}
