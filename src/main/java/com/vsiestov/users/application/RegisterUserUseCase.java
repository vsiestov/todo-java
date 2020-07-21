package com.vsiestov.users.application;

import com.vsiestov.shared.core.Result;
import com.vsiestov.shared.core.UseCaseClass;
import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.users.domain.*;
import com.vsiestov.users.mapper.UserMapper;
import com.vsiestov.users.repository.UsersRepository;
import com.vsiestov.users.rest.dto.RegistrationDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.rest.dto.UserWithTokenDTO;
import com.vsiestov.users.services.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class RegisterUserUseCase implements UseCaseClass<UserWithTokenDTO, RegistrationDTO> {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public RegisterUserUseCase(
        UsersRepository usersRepository,
        UserMapper userMapper,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        JwtService jwtService
    ) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserWithTokenDTO execute(RegistrationDTO request) {
        Result<UserEmail> emailResult = UserEmail.create(request.getEmail());
        Result<UserName> firstNameResult = UserName.create(request.getFirstName(), "Provided first name is not valid", "firstName");
        Result<UserName> lastNameResult = UserName.create(request.getLastName(), "Provided last name is not valid", "lastName");
        Result<UserPassword> passwordResult = UserPassword.create(request.getPassword());

        ArrayList<ValidationDescription> errors = Result.errors(new Result[]{
            emailResult,
            firstNameResult,
            lastNameResult,
            passwordResult
        });

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Optional<User> existedUser = usersRepository.findByEmail(emailResult.getValue());

        if (existedUser.isPresent()) {
            ValidationException validationException = new ValidationException();
            validationException.addError(new ValidationDescription("email", "A member with such email already exists"));

            throw validationException;
        }

        User user = User.builder()
            .email(emailResult.getValue())
            .password(new UserPassword(bCryptPasswordEncoder.encode(passwordResult.getValue().getValue())))
            .firstName(firstNameResult.getValue())
            .lastName(lastNameResult.getValue())
            .build();

        UserDTO userDTO = userMapper.toDTO(usersRepository.save(user));
        String token = jwtService.generateToken(userDTO);

        return userMapper.toDTO(token, userDTO);
    }
}
