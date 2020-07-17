package com.vsiestov.users.application;

import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.stereotype.Component;

@Component
public class GetUserInfoUseCase {
    private final UsersService usersService;

    public GetUserInfoUseCase(UsersService usersService) {
        this.usersService = usersService;
    }

    public UserDTO execute() {
        return usersService.getCurrentlyLoggedInUser();
    }
}
