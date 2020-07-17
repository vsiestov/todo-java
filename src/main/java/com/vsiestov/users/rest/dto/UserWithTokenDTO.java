package com.vsiestov.users.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithTokenDTO {
    private String token;
    private UserDTO user;
}
