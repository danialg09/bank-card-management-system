package com.bank.mapper;

import com.bank.dto.user.UserResponse;
import com.bank.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> users);
}
