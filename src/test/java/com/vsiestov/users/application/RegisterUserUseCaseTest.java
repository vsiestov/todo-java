package com.vsiestov.users.application;

import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.users.domain.User;
import com.vsiestov.users.domain.UserEmail;
import com.vsiestov.users.domain.UserName;
import com.vsiestov.users.mapper.UserMapper;
import com.vsiestov.users.repository.UsersRepository;
import com.vsiestov.users.rest.dto.RegistrationDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.rest.dto.UserWithTokenDTO;
import com.vsiestov.users.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserUseCaseTest {
    private UsersRepository usersRepository;
    private RegisterUserUseCase registerUserUseCase;
    private String email = "valerii.siestov@gmail.com";
    private RegistrationDTO registrationDTO;
    private User user;
    private UserEmail userEmail;

    @BeforeEach
    void setup() {
        usersRepository = mock(UsersRepository.class);
        registerUserUseCase = new RegisterUserUseCase(
            usersRepository,
            new UserMapper(),
            new BCryptPasswordEncoder(),
            new JwtService()
        );
        registrationDTO = new RegistrationDTO(
            "Valeriy",
            "Siestov",
            email,
            "Password1$"
        );
        userEmail = new UserEmail(email);
        UserName userFirstName = new UserName("Valeriy");
        UserName userLastName = new UserName("Siestov");

        user = User.builder()
            .id(13)
            .email(userEmail)
            .firstName(userFirstName)
            .lastName(userLastName)
            .build();;
    }

    @Test
    @DisplayName("It should return an exception because of an already used email")
    void shouldReturnExistedMember() {
        when(usersRepository.findByEmail(userEmail))
            .thenReturn(Optional.of(user));

        try {
            registerUserUseCase.execute(registrationDTO);
        } catch (ValidationException ex) {
            ArrayList<ValidationDescription> errors = ex.getErrors();

            assertEquals(errors.size(), 1);
            assertEquals(errors.get(0).getName(), "email");
            assertEquals(errors.get(0).getDescription(), "A member with such email already exists");
        }
    }

    @Test
    @DisplayName("It should return a newly create user")
    void shouldReturnNewlyCreatedUser() {
        UserEmail userEmail = new UserEmail(email);

        when(usersRepository.findByEmail(userEmail))
            .thenReturn(Optional.empty());

        when(usersRepository.save(any()))
            .thenReturn(user);

        UserWithTokenDTO userWithTokenDTO = registerUserUseCase.execute(registrationDTO);
        UserDTO userDTO = userWithTokenDTO.getUser();

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getEmail(), user.getEmail().getValue());
        assertEquals(userDTO.getFirstName(), user.getFirstName().getValue());
        assertEquals(userDTO.getLastName(), user.getLastName().getValue());
    }

    @Test
    @DisplayName("It should return an exception because of the invalid inputs")
    void shouldReturnInvalidArgumentsException() {
        RegistrationDTO registrationDTO = new RegistrationDTO("", "", "invalid@email", "");

        try {
            registerUserUseCase.execute(registrationDTO);
        } catch (ValidationException ex) {
            ArrayList<ValidationDescription> errors = ex.getErrors();

            assertEquals(errors.size(), 4);
            assertEquals(errors.get(0).getName(), "email");
            assertEquals(errors.get(0).getDescription(), "Provided email is not valid");

            assertEquals(errors.get(1).getName(), "firstName");
            assertEquals(errors.get(1).getDescription(), "Provided first name is not valid");

            assertEquals(errors.get(2).getName(), "lastName");
            assertEquals(errors.get(2).getDescription(), "Provided last name is not valid");

            assertEquals(errors.get(3).getName(), "password");
            assertEquals(errors.get(3).getDescription(), "Your password is not valid");
        }
    }
}
