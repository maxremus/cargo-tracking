package org.example.cargotracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "incoming_loads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomingLoad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Фирмата е задължителна")
    @Size(
            min = 2,
            max = 150,
            message = "Фирмата трябва да е между 2 и 150 символа"
    )
    @Column(nullable = false)
    private String supplierCompany;

    @NotBlank(message = "Номерът на фактурата е задължителен")
    @Size(
            min = 10,
            max = 10,
            message = "Номерът на фактурата трябва да е 10 символа"
    )
    @Column(nullable = false)
    private String invoiceNumber;

    @NotNull(message = "Изберете дата")
    @Column(nullable = false)
    private LocalDate invoiceDate;

    @NotBlank(message = "Продуктът е задължителен")
    @Size(
            min = 2,
            max = 300,
            message = "Продуктът трябва да е между 2 и 300 символа"
    )
    @Column(nullable = false)
    private String productName;

    @NotNull(message = "Количеството е задължително")
    @Min(
            value = 1,
            message = "Количеството трябва да е поне 1"
    )
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Сумата е задължителна")
    @DecimalMin(
            value = "0.01",
            message = "Сумата трябва да е по-голяма от 0"
    )
    @Column(nullable = false)
    private Double totalPrice;

    @Size(
            max = 2000,
            message = "Бележките могат да са максимум 2000 символа"
    )
    @Column(length = 2000)
    private String notes;

    private String documentPath;

    private String imagePath;

    private LocalDateTime createdAt;
}
