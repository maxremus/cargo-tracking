package org.example.cargotracking.service.Impl;

import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.repository.IncomingLoadRepository;
import org.example.cargotracking.service.IncomingLoadService;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncomingLoadServiceImpl implements IncomingLoadService {

    private final IncomingLoadRepository incomingLoadRepository;

    public IncomingLoadServiceImpl(IncomingLoadRepository incomingLoadRepository) {
        this.incomingLoadRepository = incomingLoadRepository;
    }

    @Override
    public List<IncomingLoad> findAll() {

        return incomingLoadRepository.findAll();
    }

    @Override
    public IncomingLoad findById(Long id) {

        return incomingLoadRepository
                .findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Incoming load not found"
                        )
                );
    }

    @Override
    public void save(
            IncomingLoad incomingLoad
    ) {

        if (incomingLoad.getCreatedAt() == null) {

            incomingLoad.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        boolean exists =
                incomingLoadRepository
                        .existsByInvoiceNumberAndSupplierCompany(

                                incomingLoad.getInvoiceNumber(),

                                incomingLoad.getSupplierCompany()
                        );

        if (exists && incomingLoad.getId() == null) {

            throw new RuntimeException(
                    "Вече има въведена фактура с този номер за този доставчик!"
            );
        }

        incomingLoadRepository.save(incomingLoad);
    }

    @Override
    public void delete(Long id) {

        incomingLoadRepository.deleteById(id);
    }

    @Override
    public Page<IncomingLoad> search(

            IncomingLoadSearchDTO search,

            Pageable pageable

    ) {

        List<IncomingLoad> loads =
                incomingLoadRepository.findAll();

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
                .findAll()
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
                .findAll()
                .stream()
                .map(load ->
                        load.getInvoiceNumber()
                )
                .distinct()
                .count();
    }
}
