package com.company.library.config;

import com.company.library.entity.Role;
import com.company.library.entity.User;
import com.company.library.enums.RoleName;
import com.company.library.repository.RoleRepository;
import com.company.library.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedDefaultUsers() {
        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
            .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleName.ADMIN).build()));

        Role librarianRole = roleRepository.findByRoleName(RoleName.LIBRARIAN)
            .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleName.LIBRARIAN).build()));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .fullName("System Administrator")
                .email("admin@library.com")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(Set.of(adminRole))
                .build());
        }

        if (!userRepository.existsByUsername("librarian")) {
            userRepository.save(User.builder()
                .username("librarian")
                .password(passwordEncoder.encode("librarian123"))
                .fullName("Library Librarian")
                .email("librarian@library.com")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(Set.of(librarianRole))
                .build());
        }
    }
}
