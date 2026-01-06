package com.example.rolebase.mapper;

import com.example.rolebase.config.GlobalMapperConfig;
import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface AdminRegistrationMapper {

    @Mapping(target = "roles", ignore = true)
    User toEntity(AdminRegistrationRequest request);

    UserResponse toResponse(User response);

}
