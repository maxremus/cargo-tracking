package org.example.cargotracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "trucks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Номерът на камиона е задължителен")
    @Size(min = 3, max = 20, message = "Номерът трябва да е между 3 и 20 символа")
    @Column(nullable = false, unique = true)
    private String truckNumber;

    @NotBlank(message = "Името на шофьора е задължително")
    @Size(min = 2, max = 100, message = "Името трябва да е между 2 и 100 символа")
    private String driverName;

    @Size(max = 100, message = "Моделът е твърде дълъг")
    private String model;

    @Size(max = 150, message = "Името на фирмата е твърде дълго")
    private String companyName;
}
