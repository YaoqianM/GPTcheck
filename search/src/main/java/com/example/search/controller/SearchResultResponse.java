// src/main/java/com/example/search/model/SearchResultResponse.java
package com.example.search.controller;

import com.example.search.controller.AuthorResponse;
import com.example.search.controller.BookResponse;

import java.util.List;

public class SearchResultResponse {
    private List<BookResponse>    books;
    private List<AuthorResponse>  authors;
    private String                serviceDetails;

    public SearchResultResponse() { }

    // All-args constructor (optional)
    public SearchResultResponse(
            List<BookResponse> books,
            List<AuthorResponse> authors,
            String serviceDetails
    ) {
        this.books          = books;
        this.authors        = authors;
        this.serviceDetails = serviceDetails;
    }

    // getters
    public List<BookResponse> getBooks() {
        return books;
    }
    public List<AuthorResponse> getAuthors() {
        return authors;
    }
    public String getServiceDetails() {
        return serviceDetails;
    }

    // setters
    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }
    public void setAuthors(List<AuthorResponse> authors) {
        this.authors = authors;
    }
    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }
}
