package com.company.library.mapper;

import com.company.library.dto.request.BookRequestDTO;
import com.company.library.dto.response.BookResponseDTO;
import com.company.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookResponseDTO entityToResponse(Book book);

    Book requestToEntity(BookRequestDTO bookRequestDTO);

    void updateEntityFromRequest(BookRequestDTO bookRequestDTO, @MappingTarget Book book);
}
