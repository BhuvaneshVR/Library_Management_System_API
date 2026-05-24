package com.company.library.repository;

import com.company.library.entity.Transaction;
import com.company.library.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.book.id = :bookId AND t.member.id = :memberId " +
           "AND t.status = :status")
    Optional<Transaction> findByBookIdMemberIdAndStatus(
        @Param("bookId") Long bookId,
        @Param("memberId") Long memberId,
        @Param("status") TransactionStatus status
    );

    Page<Transaction> findByMemberId(Long memberId, Pageable pageable);

    Page<Transaction> findByBookId(Long bookId, Pageable pageable);
}
