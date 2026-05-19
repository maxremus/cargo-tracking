package org.example.cargotracking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
    private String companyName;
    private String eik;
    private String address;
    private String phone;
    private String email;

    private String logo;

    private String smtpHost;
    private Integer smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private boolean smtpTls;

    private boolean notifyNewLoad;
    private boolean notifyIncomingLoad;
    private boolean dailyReports;

    private String language;
    private String timezone;
    private boolean darkMode;
}
