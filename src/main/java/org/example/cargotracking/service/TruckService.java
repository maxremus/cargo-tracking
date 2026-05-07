package org.example.cargotracking.service;

import org.example.cargotracking.entity.Truck;

import java.util.List;

public interface TruckService {

    Truck save(Truck truck);

    List<Truck> findAll();

    Truck findById(Long id);

    void delete(Long id);
}
