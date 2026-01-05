package com.example.rolebase.service;

import com.example.rolebase.dto.request.RegistrationRequest;
import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.dto.response.UpdateUserResponse;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.Role;
import com.example.rolebase.entity.User;
import com.example.rolebase.repository.RoleRepository;
import com.example.rolebase.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse toUserResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .enabled(user.isEnable())
                .build();
    }

    public User registerUser(RegistrationRequest request) {
        validateUsernameAndEmail(request.getUsername(), request.getEmail());

        User user = createUser(request.getUsername(), request.getPassword(), request.getEmail());
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException(
                        "USER role not found. Please initialize roles first."));

        user.getRoles().add(userRole);
        User savedUser = userRepository.save(user);
        log.info("User registered: {} with role: USER", savedUser.getUsername());

        return savedUser;
    }

    public User registerUserWithRole(
            String username, String password, String email, Set<String> roleNames
    ) {

        validateUsernameAndEmail(username, email);
        validateRoleNames(roleNames);

        User user = createUser(username, password, email);

        Set<Role> roles = Optional.ofNullable(roleNames)
                .orElse(Collections.emptySet())
                .stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found " + roleName)))
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new IllegalStateException("USER role not found"));
            roles.add(defaultRole);
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        String rolesStr = savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));

        log.info("User registered: {} with roles: {}", savedUser.getUsername(), rolesStr);

        return savedUser;
    }

    public UpdateUserResponse updateUser(String currentUsername, UpdateUserRequest updatedDetails) {
        User existingUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserResponse beforeUpdate = toUserResponse(existingUser);

        updateUsername(existingUser, updatedDetails.getUsername());
        updateEmail(existingUser, updatedDetails.getEmail());
        updatePassword(existingUser, updatedDetails.getPassword());

        User savedUser = userRepository.save(existingUser);
        log.info("User profile updated: {}", savedUser.getUsername());

        UserResponse afterUpdate = toUserResponse(savedUser);

        return UpdateUserResponse.builder()
                .beforeUpdate(beforeUpdate)
                .afterUpdate(afterUpdate).build();
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
        log.info("User deleted with ID: {}", userId);
    }

    // Private helper methods
    private void validateUsernameAndEmail(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
    }

    private void validateRoleNames(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return;
        }

        Set<String> validRoles = Set.of("USER", "ADMIN", "MANAGER");
        for (String roleName : roleNames) {
            if (!validRoles.contains(roleName)) {
                throw new IllegalArgumentException("Invalid role: " + roleName);
            }
        }
    }

    private User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    private void updateUsername(User existingUser, String newUsername) {
        if (newUsername != null && !newUsername.equals(existingUser.getUsername())) {
            Optional<User> foundUserWithNewUsername = userRepository.findByUsername(newUsername);
            if (foundUserWithNewUsername.isPresent()) {
                throw new IllegalArgumentException("Username is already taken");
            }
            existingUser.setUsername(newUsername);
        }
    }

    private void updateEmail(User existingUser, String newEmail) {
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            Optional<User> userWithNewEmail = userRepository.findByEmail(newEmail);
            if (userWithNewEmail.isPresent()) {
                throw new IllegalArgumentException("Email is already registered");
            }
            existingUser.setEmail(newEmail);
        }
    }

    private void updatePassword(User existingUser, String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }
    }
}