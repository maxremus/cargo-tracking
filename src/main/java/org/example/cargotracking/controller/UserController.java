package org.example.cargotracking.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.User;
import org.example.cargotracking.entity.UserRole;
import org.example.cargotracking.service.SystemLogService;
import org.example.cargotracking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SystemLogService systemLogService;

    @GetMapping
    public String users(
            Model model,
            HttpServletRequest request
    ) {

        model.addAttribute(
                "users",
                userService.findAll()
        );

        return "users";
    }

    @GetMapping("/create")
    public String createUser(
            Model model,
            HttpServletRequest request
    ) {

        User user = new User();

        user.setEnabled(true);

        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "roles",
                UserRole.values()
        );

        return "create-user";
    }

    @GetMapping("/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request
    ) {

        User user =
                userService.findById(id);

        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "roles",
                UserRole.values()
        );

        return "edit-user";
    }

    @PostMapping("/save")
    public String saveUser(

            @Valid
            @ModelAttribute("user")
            User user,

            BindingResult bindingResult,

            Model model,

            HttpServletRequest request

    ) {

        // PASSWORD VALIDATION

        if (user.getId() == null &&
                (user.getPassword() == null
                        || user.getPassword().length() < 6)) {

            bindingResult.rejectValue(
                    "password",
                    "error.user",
                    "Минимум 6 символа"
            );
        }

        // VALIDATION ERRORS

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "roles",
                    UserRole.values()
            );

            return "create-user";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        try {

            userService.save(user);

        } catch (RuntimeException e) {

            model.addAttribute(
                    "roles",
                    UserRole.values()
            );

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "create-user";
        }

        systemLogService.log(

                auth.getName(),

                "CREATE_USER",

                "Добавен потребител: "
                        + user.getUsername(),

                request.getRemoteAddr()
        );

        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(

            @Valid
            @ModelAttribute("user")
            User user,

            BindingResult bindingResult,

            Model model,

            HttpServletRequest request

    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "roles",
                    UserRole.values()
            );

            return "edit-user";
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        try {

            userService.save(user);

        } catch (RuntimeException e) {

            model.addAttribute(
                    "roles",
                    UserRole.values()
            );

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "edit-user";
        }

        systemLogService.log(

                auth.getName(),

                "UPDATE_USER",

                "Редактиран потребител: "
                        + user.getUsername(),

                request.getRemoteAddr()
        );

        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(

            @PathVariable Long id,

            HttpServletRequest request

    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUsername =
                auth.getName();

        User user =
                userService.findById(id);

        if (user.getUsername().equals(currentUsername)) {

            return "redirect:/users?error=selfdelete";
        }

        userService.delete(id);

        systemLogService.log(

                currentUsername,

                "DELETE_USER",

                "Изтрит потребител: "
                        + user.getUsername(),

                request.getRemoteAddr()
        );

        return "redirect:/users";
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(

            @PathVariable Long id,

            HttpServletRequest request

    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user =
                userService.findById(id);

        user.setEnabled(!user.isEnabled());

        userService.save(user);

        systemLogService.log(

                auth.getName(),

                "CHANGE_USER_STATUS",

                "Променен статус на потребител: "
                        + user.getUsername()
                        + " -> "
                        + (user.isEnabled()
                        ? "АКТИВЕН"
                        : "ИЗКЛЮЧЕН"),

                request.getRemoteAddr()
        );

        return "redirect:/users";
    }
}