package com.company.library.controller;

import com.company.library.dto.request.BookRequestDTO;
import com.company.library.dto.response.ApiResponseDTO;
import com.company.library.dto.response.BookResponseDTO;
import com.company.library.service.BookService;
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
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Book management API")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book in the library system")
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO book = bookService.createBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDTO.success("Book created successfully", book));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a specific book by its ID")
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Book retrieved successfully", book));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books with pagination support")
    public ResponseEntity<ApiResponseDTO<Page<BookResponseDTO>>> getAllBooks(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BookResponseDTO> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Books retrieved successfully", books));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by title, author, or ISBN")
    public ResponseEntity<ApiResponseDTO<Page<BookResponseDTO>>> searchBooks(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BookResponseDTO> books = bookService.searchBooks(keyword, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Books searched successfully", books));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book")
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO book = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Book updated successfully", book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Soft deletes a book from the system")
    public ResponseEntity<ApiResponseDTO<String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Book deleted successfully"));
    }
}
