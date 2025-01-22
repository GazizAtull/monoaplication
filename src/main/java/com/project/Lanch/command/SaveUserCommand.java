package com.project.Lanch.command;

import com.project.Lanch.model.User;
import com.project.Lanch.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


/**
 * SaveUserCommand is responsible for saving a user using UserService.
 * It handles both user creation and updating existing user details.
 * Passwords are securely encoded, and default roles are assigned if not provided.
 */


public class SaveUserCommand implements Command<User> {
    private final User user;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SaveUserCommand(User user, UserService userService, PasswordEncoder passwordEncoder) {
        this.user = user;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute() {
        if (user.getId() != null) {
            User userFromDb = userService.findUserById(user.getId());
            if (userFromDb != null) {
                userFromDb.setUsername(user.getUsername());
                userFromDb.setRoles(user.getRoles());
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                return userService.saveAndReturnUser(userFromDb);
            } else {
                throw new IllegalArgumentException("User with ID " + user.getId() + " not found.");
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                Set<String> roles = new HashSet<>();
                roles.add("USER");
                user.setRoles(roles);
            }
            return userService.saveAndReturnUser(user);
        }
    }
}
