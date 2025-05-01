package com.example.search.controller;

import java.util.List;

public class SearchResultResponse {
    private List<BookResponse> books;
    private List<AuthorResponse> authors;
    private String serviceDetails;

    // Getters and Setters
    public List<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }

    public List<AuthorResponse> getAuthors() {
        return authors;
    }

    public void setAuthors(List<com.example.search.model.AuthorResponse> authors) {
        this.authors = authors;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public void setAuthors(List<com.example.search.model.AuthorResponse> authorResponses) {
    }
}