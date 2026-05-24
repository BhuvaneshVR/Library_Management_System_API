package com.company.library.service;

import com.company.library.dto.request.MemberRequestDTO;
import com.company.library.dto.response.MemberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO);

    MemberResponseDTO getMemberById(Long id);

    Page<MemberResponseDTO> getAllMembers(Pageable pageable);

    MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDTO);

    void deactivateMember(Long id);
}
