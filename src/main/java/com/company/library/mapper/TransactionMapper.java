package com.company.library.mapper;

import com.company.library.dto.response.TransactionResponseDTO;
import com.company.library.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class, MemberMapper.class})
public interface TransactionMapper {

    TransactionResponseDTO entityToResponse(Transaction transaction);
}
