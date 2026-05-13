package org.example.cargotracking.service;

import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncomingLoadService {

    List<IncomingLoad> findAll();

    IncomingLoad findById(Long id);

    void save(IncomingLoad incomingLoad);

    void delete(Long id);

    Page<IncomingLoad> search(IncomingLoadSearchDTO search, Pageable pageable);

    long countSuppliers();

    long countInvoices();
}
