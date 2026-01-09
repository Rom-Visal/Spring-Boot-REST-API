package com.example.rolebase.mapper;

import com.example.rolebase.config.GlobalMapperConfiguration;
import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfiguration.class)
public interface UpdateUserRequestMapper {

    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);
}
