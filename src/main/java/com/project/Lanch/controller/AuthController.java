package com.project.Lanch.controller;

import com.project.Lanch.builder.UserProfileBuilder;
import com.project.Lanch.model.User;
import com.project.Lanch.model.UserProfile;
import com.project.Lanch.repository.UserProfileRepository;
import com.project.Lanch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


/**
 * AuthController handles user authentication and registration processes.
 * - Provides endpoints for login and registration pages.
 * - Handles user registration, including:
 *   - Password matching validation.
 *   - Checking for existing usernames.
 *   - Saving user data with encoded passwords.
 *   - Creating and saving user profiles using UserProfileBuilder.
 */




@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository,
                          UserProfileRepository userProfileRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "User already exists");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(Collections.singleton("USER"));

        userRepository.save(newUser);

        UserProfile userProfile = new UserProfileBuilder()
                .setUser(newUser)
                .setEmail(email)
                .setFullName(fullName)
                .setPhone(phone != null ? phone : "")
                .setAddress(address != null ? address : "")
                .build();

        userProfileRepository.save(userProfile);

        return "redirect:/register?registered=true";
    }

}
