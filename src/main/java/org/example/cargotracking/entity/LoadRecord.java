package org.example.cargotracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Продуктът е задължителен")
    @Size(min = 2, max = 100, message = "Продуктът трябва да е между 2 и 100 символа")
    @Column(nullable = false)
    private String productName;

    @NotNull(message = "Количеството е задължително")
    @Positive(message = "Количеството трябва да е положително")
    @Max(value = 1000000, message = "Твърде голямо количество")
    private Integer quantity;

    @NotBlank(message = "Полето е задължително")
    @Size(min = 2, max = 100, message = "Името трябва да е между 2 и 100 символа")
    private String loadedBy;

    @NotBlank(message = "Полето е задължително")
    @Size(min = 2, max = 100, message = "Името трябва да е между 2 и 100 символа")
    private String transportedBy;

    @NotNull(message = "Изберете камион")
    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    private LocalDateTime loadingDate;

    @PrePersist
    public void prePersist() {

        if (loadingDate == null) {
            loadingDate = LocalDateTime.now();
        }

    }

    @Size(max = 2000, message = "Бележките са твърде дълги")
    @Column(length = 2000)
    private String notes;

    private String imagePath;

    @NotNull(message = "Изберете статус")
    @Enumerated(EnumType.STRING)
    private LoadStatus status;

    private String documentPath;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
