package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.service.LoadRecordService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadRecordServiceImpl implements LoadRecordService {

    private final LoadRecordRepository loadRecordRepository;

    @Override
    public LoadRecord save(LoadRecord loadRecord) {

        return loadRecordRepository.save(loadRecord);
    }

    @Override
    public List<LoadRecord> findAll() {

        return loadRecordRepository.findAll();
    }

    @Override
    public LoadRecord findById(Long id) {

        return loadRecordRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {

        loadRecordRepository.deleteById(id);
    }


    @Override
    public LoadRecord saveLoad(
            LoadRecord loadRecord,
            MultipartFile image
    ) throws Exception {

        // existing record
        if (loadRecord.getId() != null) {

            LoadRecord existing =
                    findById(loadRecord.getId());

            // keep old image
            if (image.isEmpty()) {

                loadRecord.setImagePath(
                        existing.getImagePath()
                );
            }

            // keep old date
            loadRecord.setLoadingDate(
                    existing.getLoadingDate()
            );
        }

        // upload image
        if (!image.isEmpty()) {

            String fileName =
                    System.currentTimeMillis()
                            + "_"
                            + image.getOriginalFilename();

            Path uploadPath =
                    Paths.get("uploads");

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(
                        uploadPath
                );
            }

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.write(
                    filePath,
                    image.getBytes()
            );

            loadRecord.setImagePath(fileName);
        }

        // create date
        if (loadRecord.getLoadingDate()
                == null) {

            loadRecord.setLoadingDate(
                    LocalDateTime.now()
            );
        }

        return loadRecordRepository
                .save(loadRecord);
    }

    @Override
    public List<LoadRecord> findByStatus(LoadStatus status) {
        return loadRecordRepository.findByStatus(status);
    }
}
