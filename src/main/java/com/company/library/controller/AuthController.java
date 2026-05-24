package com.company.library.controller;

import com.company.library.dto.request.AuthRequestDTO;
import com.company.library.dto.response.ApiResponseDTO;
import com.company.library.dto.response.AuthResponseDTO;
import com.company.library.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API for login and token management")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return a JWT token")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponse = authenticationService.authenticate(authRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", authResponse));
    }
}
