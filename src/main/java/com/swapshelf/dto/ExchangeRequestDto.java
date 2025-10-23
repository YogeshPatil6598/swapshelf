package com.swapshelf.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExchangeRequestDto {
        private Long id;

        private UserDto requester;
        private BookDto book;
        private BookDto requestedExchangeBook; // Nullable
        private String status;
        private LocalDateTime createdAt;

        // Getters and setters


}
