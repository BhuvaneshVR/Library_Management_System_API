package com.company.library.controller;

import com.company.library.dto.request.MemberRequestDTO;
import com.company.library.dto.response.ApiResponseDTO;
import com.company.library.dto.response.MemberResponseDTO;
import com.company.library.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "Members", description = "Member management API")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "Create a new member", description = "Creates a new member in the library system")
    public ResponseEntity<ApiResponseDTO<MemberResponseDTO>> createMember(@Valid @RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO member = memberService.createMember(memberRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDTO.success("Member created successfully", member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID", description = "Retrieves a specific member by its ID")
    public ResponseEntity<ApiResponseDTO<MemberResponseDTO>> getMemberById(@PathVariable Long id) {
        MemberResponseDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Member retrieved successfully", member));
    }

    @GetMapping
    @Operation(summary = "Get all members", description = "Retrieves all members with pagination support")
    public ResponseEntity<ApiResponseDTO<Page<MemberResponseDTO>>> getAllMembers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberResponseDTO> members = memberService.getAllMembers(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Members retrieved successfully", members));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update member", description = "Updates an existing member")
    public ResponseEntity<ApiResponseDTO<MemberResponseDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO member = memberService.updateMember(id, memberRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Member updated successfully", member));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate member", description = "Deactivates a member")
    public ResponseEntity<ApiResponseDTO<String>> deactivateMember(@PathVariable Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Member deactivated successfully"));
    }
}
