package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.repository.UserRepository;
import org.example.cargotracking.service.SecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Company getCurrentCompany() {
        return getCurrentUser().getCompany();
    }
}
