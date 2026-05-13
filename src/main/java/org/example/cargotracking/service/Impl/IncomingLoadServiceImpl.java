package org.example.cargotracking.service.Impl;

import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.example.cargotracking.repository.IncomingLoadRepository;
import org.example.cargotracking.service.IncomingLoadService;
import org.springframework.stereotype.Service;

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

        incomingLoadRepository.save(
                incomingLoad
        );
    }

    @Override
    public void delete(Long id) {

        incomingLoadRepository.deleteById(id);
    }

    @Override
    public List<IncomingLoad> search(IncomingLoadSearchDTO search) {

        return incomingLoadRepository
                .findAll()
                .stream()

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
    }
}
