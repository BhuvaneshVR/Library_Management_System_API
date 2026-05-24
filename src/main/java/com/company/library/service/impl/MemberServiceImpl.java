package com.company.library.service.impl;

import com.company.library.dto.request.MemberRequestDTO;
import com.company.library.dto.response.MemberResponseDTO;
import com.company.library.entity.Member;
import com.company.library.enums.MemberStatus;
import com.company.library.exception.DuplicateResourceException;
import com.company.library.exception.ResourceNotFoundException;
import com.company.library.mapper.MemberMapper;
import com.company.library.repository.MemberRepository;
import com.company.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO) {
        log.info("Creating new member with email: {}", memberRequestDTO.getEmail());

        memberRepository.findByEmail(memberRequestDTO.getEmail()).ifPresent(member -> {
            throw new DuplicateResourceException("Member with email " + memberRequestDTO.getEmail() + " already exists");
        });

        Member member = memberMapper.requestToEntity(memberRequestDTO);
        member.setStatus(MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(member);

        log.info("Member created successfully with ID: {}", savedMember.getId());
        return memberMapper.entityToResponse(savedMember);
    }

    @Override
    public MemberResponseDTO getMemberById(Long id) {
        log.debug("Fetching member with ID: {}", id);
        return memberRepository.findById(id)
            .map(memberMapper::entityToResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
    }

    @Override
    public Page<MemberResponseDTO> getAllMembers(Pageable pageable) {
        log.debug("Fetching all members");
        return memberRepository.findAll(pageable).map(memberMapper::entityToResponse);
    }

    @Override
    @Transactional
    public MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDTO) {
        log.info("Updating member with ID: {}", id);

        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));

        if (!member.getEmail().equals(memberRequestDTO.getEmail())) {
            memberRepository.findByEmail(memberRequestDTO.getEmail()).ifPresent(existingMember -> {
                throw new DuplicateResourceException("Member with email " + memberRequestDTO.getEmail() + " already exists");
            });
        }

        memberMapper.updateEntityFromRequest(memberRequestDTO, member);
        Member updatedMember = memberRepository.save(member);

        log.info("Member updated successfully with ID: {}", id);
        return memberMapper.entityToResponse(updatedMember);
    }

    @Override
    @Transactional
    public void deactivateMember(Long id) {
        log.info("Deactivating member with ID: {}", id);

        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));

        member.setStatus(MemberStatus.INACTIVE);
        memberRepository.save(member);

        log.info("Member deactivated with ID: {}", id);
    }
}
