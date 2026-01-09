package com.example.rolebase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_roles",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_role",
                columnNames = {"user_id", "role_id"}))
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "role_id")
    private Role role;

    private LocalDateTime assignAt;
    private String assignBy;

    @PrePersist
    public void onCreate() {
        this.assignAt = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentPrincipalName = authentication.getName();

            if ("anonymousUser".equalsIgnoreCase(currentPrincipalName)) {
                this.assignBy = "SELF_REGISTERED";
            } else {
                this.assignBy = currentPrincipalName;
            }

        } else {
            this.assignBy = "SYSTEM";
        }
    }
}
