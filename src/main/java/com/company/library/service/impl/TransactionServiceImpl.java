package com.company.library.service.impl;

import com.company.library.dto.request.TransactionRequestDTO;
import com.company.library.dto.response.TransactionResponseDTO;
import com.company.library.entity.Book;
import com.company.library.entity.Member;
import com.company.library.entity.Transaction;
import com.company.library.enums.MemberStatus;
import com.company.library.enums.TransactionStatus;
import com.company.library.exception.BusinessException;
import com.company.library.exception.ResourceNotFoundException;
import com.company.library.mapper.TransactionMapper;
import com.company.library.repository.BookRepository;
import com.company.library.repository.MemberRepository;
import com.company.library.repository.TransactionRepository;
import com.company.library.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionResponseDTO issueBook(TransactionRequestDTO transactionRequestDTO) {
        log.info("Issuing book ID: {} to member ID: {}", transactionRequestDTO.getBookId(), transactionRequestDTO.getMemberId());

        Book book = bookRepository.findById(transactionRequestDTO.getBookId())
            .filter(b -> !b.getDeleted())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + transactionRequestDTO.getBookId()));

        Member member = memberRepository.findById(transactionRequestDTO.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + transactionRequestDTO.getMemberId()));

        validateIssueConditions(book, member);

        transactionRepository.findByBookIdMemberIdAndStatus(
            book.getId(),
            member.getId(),
            TransactionStatus.ISSUED
        ).ifPresent(transaction -> {
            throw new BusinessException("Member has already issued this book");
        });

        Transaction transaction = Transaction.builder()
            .book(book)
            .member(member)
            .issuedAt(LocalDateTime.now())
            .dueDate(transactionRequestDTO.getDueDate())
            .status(TransactionStatus.ISSUED)
            .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Book issued successfully with transaction ID: {}", savedTransaction.getId());

        return transactionMapper.entityToResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO returnBook(Long transactionId) {
        log.info("Returning book for transaction ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));

        if (!transaction.getStatus().equals(TransactionStatus.ISSUED)) {
            throw new BusinessException("Only issued books can be returned");
        }

        transaction.setReturnedAt(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.RETURNED);

        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Book returned successfully with transaction ID: {}", transactionId);

        return transactionMapper.entityToResponse(updatedTransaction);
    }

    @Override
    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        log.debug("Fetching all transactions");
        return transactionRepository.findAll(pageable).map(transactionMapper::entityToResponse);
    }

    @Override
    public Page<TransactionResponseDTO> getMemberTransactions(Long memberId, Pageable pageable) {
        log.debug("Fetching transactions for member ID: {}", memberId);

        memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));

        return transactionRepository.findByMemberId(memberId, pageable).map(transactionMapper::entityToResponse);
    }

    @Override
    public Page<TransactionResponseDTO> getBookTransactions(Long bookId, Pageable pageable) {
        log.debug("Fetching transactions for book ID: {}", bookId);

        bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        return transactionRepository.findByBookId(bookId, pageable).map(transactionMapper::entityToResponse);
    }

    private void validateIssueConditions(Book book, Member member) {
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("Book not available - no copies remaining");
        }

        if (!member.getStatus().equals(MemberStatus.ACTIVE)) {
            throw new BusinessException("Only active members can issue books");
        }
    }
}
