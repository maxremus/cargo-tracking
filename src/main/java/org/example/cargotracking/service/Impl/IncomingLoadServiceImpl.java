package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.repository.IncomingLoadRepository;
import org.example.cargotracking.service.IncomingLoadService;
import org.example.cargotracking.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomingLoadServiceImpl
        implements IncomingLoadService {

    private final IncomingLoadRepository incomingLoadRepository;
    private final SecurityService securityService;

    @Override
    public List<IncomingLoad> findAll() {

        return incomingLoadRepository.findAllByCompany(
                securityService.getCurrentCompany()
        );
    }

    @Override
    public IncomingLoad findById(Long id) {

        return incomingLoadRepository
                .findByIdAndCompany(
                        id,
                        securityService.getCurrentCompany()
                )
                .orElseThrow(() ->

                        new RuntimeException(
                                "Incoming load not found"
                        )
                );
    }

    @Override
    public void save(IncomingLoad incomingLoad) {

        Company company =
                securityService.getCurrentCompany();

        // CREATE

        if (incomingLoad.getId() == null) {

            incomingLoad.setCompany(
                    company
            );
        }

        if (incomingLoad.getCreatedAt() == null) {

            incomingLoad.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        boolean exists =
                incomingLoadRepository
                        .existsByInvoiceNumberAndSupplierCompanyAndCompany(

                                incomingLoad.getInvoiceNumber(),

                                incomingLoad.getSupplierCompany(),

                                company
                        );

        if (exists && incomingLoad.getId() == null) {

            throw new RuntimeException(
                    "Вече има въведена фактура с този номер за този доставчик!"
            );
        }

        incomingLoadRepository.save(
                incomingLoad
        );
    }

    @Override
    public void delete(Long id) {

        IncomingLoad incomingLoad =
                findById(id);

        incomingLoadRepository.delete(
                incomingLoad
        );
    }

    @Override
    public Page<IncomingLoad> search(

            IncomingLoadSearchDTO search,

            Pageable pageable

    ) {

        List<IncomingLoad> loads =
                incomingLoadRepository.findAllByCompany(
                        securityService.getCurrentCompany()
                );

        List<IncomingLoad> filtered =
                loads.stream()

                        .filter(i ->

                                search.getSupplierCompany() == null
                                        ||
                                        search.getSupplierCompany().isBlank()
                                        ||
                                        i.getSupplierCompany()
                                                .toLowerCase()
                                                .contains(
                                                        search.getSupplierCompany()
                                                                .toLowerCase()
                                                )
                        )

                        .filter(i ->

                                search.getInvoiceNumber() == null
                                        ||
                                        search.getInvoiceNumber().isBlank()
                                        ||
                                        i.getInvoiceNumber()
                                                .toLowerCase()
                                                .contains(
                                                        search.getInvoiceNumber()
                                                                .toLowerCase()
                                                )
                        )

                        .filter(i ->

                                search.getProductName() == null
                                        ||
                                        search.getProductName().isBlank()
                                        ||
                                        i.getProductName()
                                                .toLowerCase()
                                                .contains(
                                                        search.getProductName()
                                                                .toLowerCase()
                                                )
                        )

                        .filter(i ->

                                search.getFromDate() == null
                                        ||
                                        !i.getInvoiceDate()
                                                .isBefore(
                                                        search.getFromDate()
                                                )
                        )

                        .filter(i ->

                                search.getToDate() == null
                                        ||
                                        !i.getInvoiceDate()
                                                .isAfter(
                                                        search.getToDate()
                                                )
                        )

                        .toList();

        int start =
                (int) pageable.getOffset();

        int end =
                Math.min(
                        start + pageable.getPageSize(),
                        filtered.size()
                );

        List<IncomingLoad> pageContent =
                filtered.subList(start, end);

        return new PageImpl<>(

                pageContent,

                pageable,

                filtered.size()
        );
    }

    @Override
    public long countSuppliers() {

        return incomingLoadRepository
                .findAllByCompany(
                        securityService.getCurrentCompany()
                )
                .stream()
                .map(load ->
                        load.getSupplierCompany()
                )
                .distinct()
                .count();
    }

    @Override
    public long countInvoices() {

        return incomingLoadRepository
                .findAllByCompany(
                        securityService.getCurrentCompany()
                )
                .stream()
                .map(load ->
                        load.getInvoiceNumber()
                )
                .distinct()
                .count();
    }
}
