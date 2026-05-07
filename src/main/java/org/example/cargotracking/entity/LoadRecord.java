package org.example.cargotracking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "load_records")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    private Integer quantity;

    private String loadedBy;

    private String transportedBy;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    private LocalDateTime loadingDate;

    @Column(length = 2000)
    private String notes;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private LoadStatus status;
}
