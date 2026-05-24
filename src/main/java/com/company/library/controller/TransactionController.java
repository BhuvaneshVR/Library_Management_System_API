package com.company.library.controller;

import com.company.library.dto.request.TransactionRequestDTO;
import com.company.library.dto.response.ApiResponseDTO;
import com.company.library.dto.response.TransactionResponseDTO;
import com.company.library.service.TransactionService;
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
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Transaction management API (Issue/Return books)")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/issue")
    @Operation(summary = "Issue a book", description = "Issues a book to a member")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> issueBook(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transaction = transactionService.issueBook(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDTO.success("Book issued successfully", transaction));
    }

    @PostMapping("/{transactionId}/return")
    @Operation(summary = "Return a book", description = "Returns an issued book")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> returnBook(@PathVariable Long transactionId) {
        TransactionResponseDTO transaction = transactionService.returnBook(transactionId);
        return ResponseEntity.ok(ApiResponseDTO.success("Book returned successfully", transaction));
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves all transactions with pagination support")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getAllTransactions(
            @PageableDefault(size = 10, sort = "issuedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TransactionResponseDTO> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/members/{memberId}")
    @Operation(summary = "Get member transactions", description = "Retrieves all transactions for a specific member")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getMemberTransactions(
            @PathVariable Long memberId,
            @PageableDefault(size = 10, sort = "issuedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TransactionResponseDTO> transactions = transactionService.getMemberTransactions(memberId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Member transactions retrieved successfully", transactions));
    }

    @GetMapping("/books/{bookId}")
    @Operation(summary = "Get book transactions", description = "Retrieves all transactions for a specific book")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getBookTransactions(
            @PathVariable Long bookId,
            @PageableDefault(size = 10, sort = "issuedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TransactionResponseDTO> transactions = transactionService.getBookTransactions(bookId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Book transactions retrieved successfully", transactions));
    }
}
