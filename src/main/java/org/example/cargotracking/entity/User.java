package org.example.cargotracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Username е задължителен")
    @Size(min = 3, max = 50, message = "Username трябва да е между 3 и 50 символа")
    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false)
    private String password;

    @NotNull(message = "Изберете роля")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean enabled = true;
}
