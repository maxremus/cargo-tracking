package org.example.cargotracking.service;

import org.example.cargotracking.dto.IncomingLoadSearchDTO;
import org.example.cargotracking.entity.IncomingLoad;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface IncomingLoadService {

    List<IncomingLoad> findAll();

    IncomingLoad findById(Long id);

    void save(IncomingLoad incomingLoad);

    void delete(Long id);

    List<IncomingLoad> search(IncomingLoadSearchDTO search);
}
