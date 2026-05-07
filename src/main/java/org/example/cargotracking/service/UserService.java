package org.example.cargotracking.service;

import org.example.cargotracking.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

    List<User> findAll();
}
