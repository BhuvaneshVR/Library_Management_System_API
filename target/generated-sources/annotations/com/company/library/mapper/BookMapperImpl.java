package com.company.library.mapper;

import com.company.library.dto.request.BookRequestDTO;
import com.company.library.dto.response.BookResponseDTO;
import com.company.library.entity.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T08:42:03+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookResponseDTO entityToResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponseDTO.BookResponseDTOBuilder bookResponseDTO = BookResponseDTO.builder();

        bookResponseDTO.id( book.getId() );
        bookResponseDTO.title( book.getTitle() );
        bookResponseDTO.author( book.getAuthor() );
        bookResponseDTO.isbn( book.getIsbn() );
        bookResponseDTO.category( book.getCategory() );
        bookResponseDTO.totalCopies( book.getTotalCopies() );
        bookResponseDTO.availableCopies( book.getAvailableCopies() );
        bookResponseDTO.shelfLocation( book.getShelfLocation() );
        bookResponseDTO.deleted( book.getDeleted() );
        bookResponseDTO.createdAt( book.getCreatedAt() );
        bookResponseDTO.updatedAt( book.getUpdatedAt() );

        return bookResponseDTO.build();
    }

    @Override
    public Book requestToEntity(BookRequestDTO bookRequestDTO) {
        if ( bookRequestDTO == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.title( bookRequestDTO.getTitle() );
        book.author( bookRequestDTO.getAuthor() );
        book.isbn( bookRequestDTO.getIsbn() );
        book.category( bookRequestDTO.getCategory() );
        book.totalCopies( bookRequestDTO.getTotalCopies() );
        book.availableCopies( bookRequestDTO.getAvailableCopies() );
        book.shelfLocation( bookRequestDTO.getShelfLocation() );

        return book.build();
    }

    @Override
    public void updateEntityFromRequest(BookRequestDTO bookRequestDTO, Book book) {
        if ( bookRequestDTO == null ) {
            return;
        }

        book.setTitle( bookRequestDTO.getTitle() );
        book.setAuthor( bookRequestDTO.getAuthor() );
        book.setIsbn( bookRequestDTO.getIsbn() );
        book.setCategory( bookRequestDTO.getCategory() );
        book.setTotalCopies( bookRequestDTO.getTotalCopies() );
        book.setAvailableCopies( bookRequestDTO.getAvailableCopies() );
        book.setShelfLocation( bookRequestDTO.getShelfLocation() );
    }
}
