package com.company.library.mapper;

import com.company.library.dto.request.MemberRequestDTO;
import com.company.library.dto.response.MemberResponseDTO;
import com.company.library.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponseDTO entityToResponse(Member member);

    Member requestToEntity(MemberRequestDTO memberRequestDTO);

    void updateEntityFromRequest(MemberRequestDTO memberRequestDTO, @MappingTarget Member member);
}
