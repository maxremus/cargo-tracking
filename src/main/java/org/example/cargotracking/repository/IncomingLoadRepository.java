package org.example.cargotracking.repository;

import org.example.cargotracking.entity.IncomingLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingLoadRepository extends JpaRepository<IncomingLoad, Long> {
}
