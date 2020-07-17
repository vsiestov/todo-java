package com.vsiestov.users.services;

import com.vsiestov.shared.core.Result;
import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.users.domain.CustomUserDetails;
import com.vsiestov.users.domain.User;
import com.vsiestov.users.domain.UserEmail;
import com.vsiestov.users.mapper.UserMapper;
import com.vsiestov.users.repository.UsersRepository;
import com.vsiestov.users.rest.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    public UsersService(
        UsersRepository usersRepository,
        UserMapper userMapper
    ) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public User retrieveUserByEmail(String email) {
        Result<UserEmail> resultEmail = UserEmail.create(email);

        if (resultEmail.isFailure()) {
            ValidationException validationException = new ValidationException();
            validationException.addError(new ValidationDescription("email", "Provided email is not valid"));

            throw validationException;
        }

        Optional<User> userOptional = usersRepository.findByEmail(resultEmail.getValue());

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        return userOptional.get();
    }

    public UserDTO getUserByEmail(String email) {
        return userMapper.toDTO(retrieveUserByEmail(email));
    }

    public UserDTO getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String)authentication.getPrincipal();

        return getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = retrieveUserByEmail(email);

        return new CustomUserDetails(
            user.getEmail().getValue(),
            user.getPassword().getValue(),
            Collections.emptyList(),
            userMapper.toDTO(user)
        );
    }
}
