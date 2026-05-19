package org.example.cargotracking.service;

import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.User;


public interface SecurityService {

    User getCurrentUser();

    Company getCurrentCompany();
}
