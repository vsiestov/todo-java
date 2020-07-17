package com.vsiestov.users.rest;

import com.vsiestov.platform.SecurityConstants;
import com.vsiestov.shared.core.ValidationErrorDTO;
import com.vsiestov.users.application.GetUserInfoUseCase;
import com.vsiestov.users.application.RegisterUserUseCase;
import com.vsiestov.users.rest.dto.AuthDTO;
import com.vsiestov.users.rest.dto.RegistrationDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.rest.dto.UserWithTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Users", description = "Users API")
@RestController
public class UsersResources {
    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;

    public UsersResources(
        RegisterUserUseCase registerUserUseCase,
        GetUserInfoUseCase getUserInfoUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }

    @Operation(summary = "Login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "", content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserWithTokenDTO.class)
            )
        }),
        @ApiResponse(responseCode = "422", description = "Validation errors", content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ValidationErrorDTO.class)
            )
        })
    })
    @PostMapping(SecurityConstants.AUTH_LOGIN_URL)
    public ResponseEntity<UserWithTokenDTO> login(AuthDTO authDTO) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "", content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDTO.class)
            )
        }),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUserInfo() {
        return new ResponseEntity<>(getUserInfoUseCase.execute(), HttpStatus.OK);
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "", content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserWithTokenDTO.class)
            )
        }),
        @ApiResponse(responseCode = "422", description = "Validation errors", content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ValidationErrorDTO.class)
            )
        })
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserWithTokenDTO> registration(@RequestBody @Valid RegistrationDTO registrationDTO) {
        return new ResponseEntity<>(registerUserUseCase.execute(registrationDTO), HttpStatus.OK);
    }
}
