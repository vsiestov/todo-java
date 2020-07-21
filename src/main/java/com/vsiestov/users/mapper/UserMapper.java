package com.vsiestov.users.mapper;

import com.vsiestov.users.domain.User;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.rest.dto.UserWithTokenDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail().getValue())
            .lastName(user.getLastName().getValue())
            .firstName(user.getFirstName().getValue())
            .build();
    }

    public UserWithTokenDTO toDTO(String token, User user) {
        return new UserWithTokenDTO(token, toDTO(user));
    }

    public UserWithTokenDTO toDTO(String token, UserDTO user) {
        return new UserWithTokenDTO(token, user);
    }
}
