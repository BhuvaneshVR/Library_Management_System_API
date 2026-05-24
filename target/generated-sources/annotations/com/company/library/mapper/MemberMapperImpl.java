package com.company.library.mapper;

import com.company.library.dto.request.MemberRequestDTO;
import com.company.library.dto.response.MemberResponseDTO;
import com.company.library.entity.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T08:42:03+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberResponseDTO entityToResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDTO.MemberResponseDTOBuilder memberResponseDTO = MemberResponseDTO.builder();

        memberResponseDTO.id( member.getId() );
        memberResponseDTO.name( member.getName() );
        memberResponseDTO.email( member.getEmail() );
        memberResponseDTO.status( member.getStatus() );
        memberResponseDTO.createdAt( member.getCreatedAt() );
        memberResponseDTO.updatedAt( member.getUpdatedAt() );

        return memberResponseDTO.build();
    }

    @Override
    public Member requestToEntity(MemberRequestDTO memberRequestDTO) {
        if ( memberRequestDTO == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( memberRequestDTO.getName() );
        member.email( memberRequestDTO.getEmail() );

        return member.build();
    }

    @Override
    public void updateEntityFromRequest(MemberRequestDTO memberRequestDTO, Member member) {
        if ( memberRequestDTO == null ) {
            return;
        }

        member.setName( memberRequestDTO.getName() );
        member.setEmail( memberRequestDTO.getEmail() );
    }
}
