package com.example.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorResponse {
    private String bookName;
    private String authorName;
    private String genre;
    // Add more fields as needed
}
