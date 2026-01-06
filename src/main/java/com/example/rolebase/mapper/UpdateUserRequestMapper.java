package com.example.rolebase.mapper;

import com.example.rolebase.config.GlobalMapperConfig;
import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface UpdateUserRequestMapper {

    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);
}
