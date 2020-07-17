package com.vsiestov.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsiestov.shared.core.ErrorDTO;
import com.vsiestov.shared.core.ValidationErrorDTO;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.users.domain.CustomUserDetails;
import com.vsiestov.users.rest.dto.AuthDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.rest.dto.UserWithTokenDTO;
import com.vsiestov.users.services.JwtService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    JwtAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        AuthDTO authDTO = new ObjectMapper().readValue(request.getInputStream(), AuthDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            authDTO.getEmail(),
            authDTO.getPassword()
        );

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        CustomUserDetails user = (CustomUserDetails)authResult.getPrincipal();
        UserDTO userDTO = user.getUserDTO();
        String token = jwtService.generateToken(userDTO);

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getWriter(), new UserWithTokenDTO(token, userDTO));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value());

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());

        if (failed instanceof BadCredentialsException ) {
            validationErrorDTO.addError(new ErrorDTO("Your email or password is not valid", "email/password"));

            new ObjectMapper().writeValue(response.getWriter(), validationErrorDTO);

            return;
        }

        Throwable throwable = failed.getCause();

        if (throwable instanceof ValidationException) {
            ValidationException exception = (ValidationException) throwable;

            exception.getErrors()
                .forEach(e -> validationErrorDTO.addError(new ErrorDTO(e.getDescription(), e.getName())));


            new ObjectMapper().writeValue(response.getWriter(), validationErrorDTO);

            return;
        }


        super.unsuccessfulAuthentication(request, response, failed);
    }
}
