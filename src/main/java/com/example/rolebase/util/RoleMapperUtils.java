package com.example.rolebase.util;

import com.example.rolebase.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperUtils {

    public String fromRole(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        return userRole.getRole().getName();
    }
}
