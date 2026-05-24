package com.company.library.service.impl;

import com.company.library.dto.request.BookRequestDTO;
import com.company.library.dto.response.BookResponseDTO;
import com.company.library.entity.Book;
import com.company.library.exception.BusinessException;
import com.company.library.exception.DuplicateResourceException;
import com.company.library.exception.ResourceNotFoundException;
import com.company.library.mapper.BookMapper;
import com.company.library.repository.BookRepository;
import com.company.library.service.BookService;
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
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        log.info("Creating new book with ISBN: {}", bookRequestDTO.getIsbn());

        if (availableCopiesExceedsTotalCopies(bookRequestDTO.getAvailableCopies(), bookRequestDTO.getTotalCopies())) {
            throw new BusinessException("Available copies cannot exceed total copies");
        }

        bookRepository.findByIsbnAndDeletedFalse(bookRequestDTO.getIsbn()).ifPresent(book -> {
            throw new DuplicateResourceException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists");
        });

        Book book = bookMapper.requestToEntity(bookRequestDTO);
        book.setDeleted(false);
        Book savedBook = bookRepository.save(book);

        log.info("Book created successfully with ID: {}", savedBook.getId());
        return bookMapper.entityToResponse(savedBook);
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        log.debug("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
            .filter(book -> !book.getDeleted())
            .map(bookMapper::entityToResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        log.debug("Fetching all books");
        return bookRepository.findAllByDeletedFalse(pageable).map(bookMapper::entityToResponse);
    }

    @Override
    public Page<BookResponseDTO> searchBooks(String keyword, Pageable pageable) {
        log.debug("Searching books with keyword: {}", keyword);
        return bookRepository.searchBooks(keyword, pageable).map(bookMapper::entityToResponse);
    }

    @Override
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        log.info("Updating book with ID: {}", id);

        Book book = bookRepository.findById(id)
            .filter(b -> !b.getDeleted())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        if (availableCopiesExceedsTotalCopies(bookRequestDTO.getAvailableCopies(), bookRequestDTO.getTotalCopies())) {
            throw new BusinessException("Available copies cannot exceed total copies");
        }

        if (!book.getIsbn().equals(bookRequestDTO.getIsbn())) {
            bookRepository.findByIsbnAndDeletedFalse(bookRequestDTO.getIsbn()).ifPresent(existingBook -> {
                throw new DuplicateResourceException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists");
            });
        }

        bookMapper.updateEntityFromRequest(bookRequestDTO, book);
        Book updatedBook = bookRepository.save(book);

        log.info("Book updated successfully with ID: {}", id);
        return bookMapper.entityToResponse(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);

        Book book = bookRepository.findById(id)
            .filter(b -> !b.getDeleted())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        book.setDeleted(true);
        bookRepository.save(book);

        log.info("Book deleted (soft delete) with ID: {}", id);
    }

    private boolean availableCopiesExceedsTotalCopies(Integer availableCopies, Integer totalCopies) {
        return availableCopies > totalCopies;
    }
}
