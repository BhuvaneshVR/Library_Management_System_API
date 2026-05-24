package com.company.library.mapper;

import com.company.library.dto.response.TransactionResponseDTO;
import com.company.library.entity.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T08:42:02+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public TransactionResponseDTO entityToResponse(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionResponseDTO.TransactionResponseDTOBuilder transactionResponseDTO = TransactionResponseDTO.builder();

        transactionResponseDTO.id( transaction.getId() );
        transactionResponseDTO.book( bookMapper.entityToResponse( transaction.getBook() ) );
        transactionResponseDTO.member( memberMapper.entityToResponse( transaction.getMember() ) );
        transactionResponseDTO.issuedAt( transaction.getIssuedAt() );
        transactionResponseDTO.dueDate( transaction.getDueDate() );
        transactionResponseDTO.returnedAt( transaction.getReturnedAt() );
        transactionResponseDTO.status( transaction.getStatus() );
        transactionResponseDTO.createdAt( transaction.getCreatedAt() );

        return transactionResponseDTO.build();
    }
}
