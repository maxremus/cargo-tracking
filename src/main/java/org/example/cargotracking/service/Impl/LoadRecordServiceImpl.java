package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.example.cargotracking.repository.LoadRecordRepository;
import org.example.cargotracking.service.LoadRecordService;
import org.example.cargotracking.webConfig.LoadSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoadRecordServiceImpl
        implements LoadRecordService {

    private final LoadRecordRepository
            loadRecordRepository;

    private static final long
            MAX_FILE_SIZE =
            10 * 1024 * 1024;

    private static final Set<String>
            ALLOWED_IMAGE_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private static final Set<String>
            ALLOWED_DOCUMENT_TYPES =
            Set.of(
                    "application/pdf",

                    "application/msword",

                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

    @Override
    public List<LoadRecord> findAll() {

        return loadRecordRepository.findAll();
    }

    @Override
    public LoadRecord findById(Long id) {

        return loadRecordRepository
                .findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Load not found"
                        )
                );
    }

    @Override
    public void saveLoad(

            LoadRecord loadRecord,

            MultipartFile image,

            MultipartFile document

    ) throws Exception {

        // UPDATE

        if (loadRecord.getId() != null) {

            LoadRecord existing =
                    findById(loadRecord.getId());

            loadRecord.setCreatedBy(
                    existing.getCreatedBy()
            );

            // KEEP OLD DATE IF NULL

            if (loadRecord.getLoadingDate()
                    == null) {

                loadRecord.setLoadingDate(
                        existing.getLoadingDate()
                );
            }

            // KEEP OLD IMAGE

            if ((image == null
                    || image.isEmpty())
                    &&
                    existing.getImagePath()
                            != null) {

                loadRecord.setImagePath(
                        existing.getImagePath()
                );
            }

            // KEEP OLD DOCUMENT

            if ((document == null
                    || document.isEmpty())
                    &&
                    existing.getDocumentPath()
                            != null) {

                loadRecord.setDocumentPath(
                        existing.getDocumentPath()
                );
            }
        }

        // IMAGE VALIDATION

        if (image != null
                &&
                !image.isEmpty()) {

            validateFileSize(image);

            if (!ALLOWED_IMAGE_TYPES
                    .contains(
                            image.getContentType()
                    )) {

                throw new RuntimeException(
                        "Invalid image type"
                );
            }

            String originalFilename =
                    image.getOriginalFilename();

            String extension = "";

            if (originalFilename != null
                    &&
                    originalFilename.contains(".")) {

                extension =
                        originalFilename.substring(
                                originalFilename.lastIndexOf(".")
                        );
            }

            String imageName =
                    UUID.randomUUID()
                            + "_image"
                            + extension;

            Path imagePath =
                    Paths.get(
                            "uploads",
                            imageName
                    );

            Files.createDirectories(
                    imagePath.getParent()
            );

            Files.write(
                    imagePath,
                    image.getBytes()
            );

            loadRecord.setImagePath(
                    imageName
            );
        }

        // DOCUMENT VALIDATION

        if (document != null
                &&
                !document.isEmpty()) {

            validateFileSize(document);

            if (!ALLOWED_DOCUMENT_TYPES
                    .contains(
                            document.getContentType()
                    )) {

                throw new RuntimeException(
                        "Invalid document type"
                );
            }

            String originalFilename =
                    document.getOriginalFilename();

            String extension = "";

            if (originalFilename != null
                    &&
                    originalFilename.contains(".")) {

                extension =
                        originalFilename.substring(
                                originalFilename.lastIndexOf(".")
                        );
            }

            String documentName =
                    UUID.randomUUID()
                            + "_document"
                            + extension;

            Path documentPath =
                    Paths.get(
                            "uploads/documents",
                            documentName
                    );

            Files.createDirectories(
                    documentPath.getParent()
            );

            Files.write(
                    documentPath,
                    document.getBytes()
            );

            loadRecord.setDocumentPath(
                    documentName
            );
        }

        // DEFAULT DATE

        if (loadRecord.getLoadingDate()
                == null) {

            loadRecord.setLoadingDate(
                    LocalDateTime.now()
            );
        }

        loadRecordRepository.save(
                loadRecord
        );
    }

    private void validateFileSize(
            MultipartFile file
    ) {

        if (file.getSize()
                > MAX_FILE_SIZE) {

            throw new RuntimeException(
                    "File too large"
            );
        }
    }

    @Override
    public Page<LoadRecord> searchLoads(

            LoadSearchDTO search,

            Pageable pageable

    ) {

        return loadRecordRepository.findAll(

                LoadSpecification
                        .filterLoads(search),

                pageable
        );
    }

    @Override
    public void delete(Long id) {

        loadRecordRepository.deleteById(id);
    }

    @Override
    public List<LoadRecord> findByStatus(
            LoadStatus status
    ) {

        return loadRecordRepository
                .findAll()
                .stream()
                .filter(load ->

                        load.getStatus()
                                == status
                )
                .toList();
    }
}
