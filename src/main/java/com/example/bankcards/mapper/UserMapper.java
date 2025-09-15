package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> users);
}
