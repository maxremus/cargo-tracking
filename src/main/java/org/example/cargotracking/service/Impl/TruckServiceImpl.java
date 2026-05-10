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

        // CREATE

        if (truck.getId() == null) {

            boolean exists = truckRepository
                    .findAll()
                    .stream()
                    .anyMatch(t ->

                            t.getTruckNumber()
                                    .equalsIgnoreCase(
                                            truck.getTruckNumber()
                                    )
                    );

            if (exists) {

                throw new RuntimeException(
                        "Truck number already exists"
                );
            }

            return truckRepository.save(truck);
        }

        // UPDATE

        Truck existingTruck =
                truckRepository.findById(
                        truck.getId()
                ).orElseThrow(() ->

                        new RuntimeException(
                                "Truck not found"
                        )
                );

        existingTruck.setTruckNumber(
                truck.getTruckNumber()
        );

        existingTruck.setDriverName(
                truck.getDriverName()
        );

        existingTruck.setModel(
                truck.getModel()
        );

        existingTruck.setCompanyName(
                truck.getCompanyName()
        );

        return truckRepository.save(
                existingTruck
        );
    }

    @Override
    public List<Truck> findAll() {

        return truckRepository.findAll();
    }

    @Override
    public Truck findById(Long id) {

        return truckRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Truck not found"
                        )
                );
    }

    @Override
    public void delete(Long id) {

        Truck truck =
                truckRepository.findById(id)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Truck not found"
                                )
                        );

        truckRepository.delete(truck);
    }
}
