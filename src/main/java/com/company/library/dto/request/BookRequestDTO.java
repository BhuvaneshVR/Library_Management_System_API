package com.company.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 100, message = "ISBN must be between 10 and 100 characters")
    private String isbn;

    @Size(max = 100, message = "Category must be at most 100 characters")
    private String category;

    @NotNull(message = "Total copies is required")
    @PositiveOrZero(message = "Total copies must be positive or zero")
    private Integer totalCopies;

    @NotNull(message = "Available copies is required")
    @PositiveOrZero(message = "Available copies must be positive or zero")
    private Integer availableCopies;

    @Size(max = 100, message = "Shelf location must be at most 100 characters")
    private String shelfLocation;
}
