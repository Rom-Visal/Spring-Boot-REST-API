package com.example.rolebase.service;

import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.request.RegistrationRequest;
import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.dto.response.UpdateUserResponse;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.Role;
import com.example.rolebase.entity.User;
import com.example.rolebase.mapper.AdminRegistrationMapper;
import com.example.rolebase.mapper.UpdateUserRequestMapper;
import com.example.rolebase.mapper.UserMapper;
import com.example.rolebase.repository.RoleRepository;
import com.example.rolebase.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;
    private final AdminRegistrationMapper adminMapper;
    private final UpdateUserRequestMapper updateMapper;

    public UserResponse toUserResponse(User user) {
        return userMapper.toResponse(user);
    }

    public User registerUser(RegistrationRequest request) {
        validateUsernameAndEmail(request.getUsername(), request.getEmail());

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not found."));

        user.addRole(defaultRole);
        return userRepository.save(user);
    }

    public User registerUserByAdmin(AdminRegistrationRequest request) {
        validateUsernameAndEmail(request.getUsername(), request.getEmail());

        User user = adminMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            request.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Role not found: " + roleName));
                user.addRole(role);
            });
        } else {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new IllegalStateException("USER role not found"));
            user.addRole(defaultRole);
        }
        return userRepository.save(user);
    }

    public UpdateUserResponse updateUser(String currentUsername, UpdateUserRequest updatedDetails) {
        User existingUser = userRepository.findByUsernameWithRolesIgnoreCase(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserResponse beforeUpdate = userMapper.toResponse(existingUser);

        updateMapper.updateUserFromRequest(updatedDetails, existingUser);

        if (updatedDetails.getPassword() != null && !updatedDetails.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedDetails.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);
        return UpdateUserResponse.builder()
                .beforeUpdate(beforeUpdate)
                .afterUpdate(userMapper.toResponse(savedUser))
                .build();
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
        log.info("User deleted with ID: {}", userId);
    }

    private void validateUsernameAndEmail(String username, String email) {
        if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
    }
}