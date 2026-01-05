package com.example.rolebase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.rolebase.entity.User;
import com.example.rolebase.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.security.core.userdetails.User.builder;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        log.debug("User found: {}", user.getUsername());
        log.debug("Enabled: {}", user.isEnable());
        log.debug("Roles count: {}", user.getRoles().size());

        var authorities = user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                .toList();

        return builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnable())
                .authorities(authorities)
                .build();
    }
}