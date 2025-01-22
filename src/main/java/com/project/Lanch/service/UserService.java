package com.project.Lanch.service;


import com.project.Lanch.model.User;
import com.project.Lanch.model.UserProfile;
import com.project.Lanch.repository.UserProfileRepository;
import com.project.Lanch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService manages operations related to users and their profiles.
 * - Functions:
 *   - `findAllUsers`: Retrieves all users from the database.
 *   - `findByUsername`: Finds a user by their username.
 *   - `findUserProfileByUsername`: Retrieves a user's profile by their username.
 *   - `saveUserProfile`: Saves or updates a user profile in the database.
 *   - `findUserById`: Finds a user by their ID.
 *   - `deleteUser`: Deletes a user by their ID.
 *   - `findUserProfileById`: Retrieves a user's profile by their ID.
 *   - `saveAndReturnUser`: Saves and returns a user after persisting changes.
 * - Dependencies:
 *   - UserRepository for user-related database operations.
 *   - UserProfileRepository for profile-related database operations.
 */



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserProfile findUserProfileByUsername(String username) {
        return userProfileRepository.findByUserUsername(username);
    }

    public void saveUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public UserProfile findUserProfileById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }
    public User saveAndReturnUser(User user) {
        return userRepository.save(user);
    }

}
