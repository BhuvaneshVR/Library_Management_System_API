package com.company.library.service;

import com.company.library.dto.request.AuthRequestDTO;
import com.company.library.dto.response.AuthResponseDTO;

public interface AuthenticationService {
    AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO);
}
