package com.company.library.service.impl;

import com.company.library.dto.request.AuthRequestDTO;
import com.company.library.dto.response.AuthResponseDTO;
import com.company.library.entity.User;
import com.company.library.repository.UserRepository;
import com.company.library.security.JwtService;
import com.company.library.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequestDTO.getUsername(),
                authRequestDTO.getPassword()
            )
        );

        User user = userRepository.findByUsername(authRequestDTO.getUsername())
            .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                "User not found with username: " + authRequestDTO.getUsername()
            ));

        String token = jwtService.generateToken(authentication);

        return AuthResponseDTO.builder()
            .token(token)
            .username(user.getUsername())
            .roles(user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList())
            .build();
    }
}
