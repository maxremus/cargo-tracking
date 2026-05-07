package org.example.cargotracking.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String truckNumber;

    private String driverName;

    private String model;

    private String companyName;
}
