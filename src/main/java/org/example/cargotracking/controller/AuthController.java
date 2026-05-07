package org.example.cargotracking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @GetMapping("/layout")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getServletPath());
        return "layout";
    }
}
