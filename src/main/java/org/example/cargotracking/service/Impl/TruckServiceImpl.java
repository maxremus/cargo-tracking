package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.Truck;
import org.example.cargotracking.repository.TruckRepository;
import org.example.cargotracking.service.TruckService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    @Override
    public Truck save(Truck truck) {
        return truckRepository.save(truck);
    }

    @Override
    public List<Truck> findAll() {
        return truckRepository.findAll();
    }

    @Override
    public Truck findById(Long id) {

        return truckRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        truckRepository.deleteById(id);
    }
}
