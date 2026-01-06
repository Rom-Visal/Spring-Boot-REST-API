package com.example.rolebase.util;

import com.example.rolebase.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperUtils {

    public String fromRole(Role role) {
        if (role == null) {
            return null;
        }
        return role.getName();
    }
}
