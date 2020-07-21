package com.vsiestov.users.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
}
