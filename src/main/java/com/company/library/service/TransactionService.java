package com.company.library.service;

import com.company.library.dto.request.TransactionRequestDTO;
import com.company.library.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponseDTO issueBook(TransactionRequestDTO transactionRequestDTO);

    TransactionResponseDTO returnBook(Long transactionId);

    Page<TransactionResponseDTO> getAllTransactions(Pageable pageable);

    Page<TransactionResponseDTO> getMemberTransactions(Long memberId, Pageable pageable);

    Page<TransactionResponseDTO> getBookTransactions(Long bookId, Pageable pageable);
}
