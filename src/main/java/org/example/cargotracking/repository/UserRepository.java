package org.example.cargotracking.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"company"})
    Optional<User> findByUsername(String username);

    List<User> findAllByCompany(Company company);

    Optional<User> findByIdAndCompany(Long id, Company company);

    boolean existsByUsernameAndCompany(String username, Company company);

    Optional<User> findByUsernameAndCompany(String username, Company currentCompany);
}
