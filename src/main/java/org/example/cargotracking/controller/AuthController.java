package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final SystemLogService systemLogService;

    @GetMapping("/login")
    public String loginPage() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth != null &&
                auth.isAuthenticated() &&
                !auth.getName().equals("anonymousUser")) {

            return "redirect:/dashboard";
        }

        return "login";
    }

    @GetMapping("/layout")
    public String layout(
            Model model,
            HttpServletRequest request
    ) {

        return "layout";
    }

    @GetMapping("/login-success")
    public String loginSuccess(
            HttpServletRequest request
    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        systemLogService.log(

                auth.getName(),

                "LOGIN",

                "Успешен вход в системата",

                request.getRemoteAddr()
        );

        return "redirect:/dashboard";
    }
}