package com.vsiestov.users.rest.dto;

import com.vsiestov.shared.regexp.RegExpResources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(
        regexp = RegExpResources.PASSWORD_PATTERN,
        message = "Password must be between 8 - 40 characters, include one uppercase letter and special symbol"
    )
    private String password;
}
