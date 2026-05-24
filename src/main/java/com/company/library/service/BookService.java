package com.company.library.service;

import com.company.library.dto.request.BookRequestDTO;
import com.company.library.dto.response.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);

    BookResponseDTO getBookById(Long id);

    Page<BookResponseDTO> getAllBooks(Pageable pageable);

    Page<BookResponseDTO> searchBooks(String keyword, Pageable pageable);

    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    void deleteBook(Long id);
}
