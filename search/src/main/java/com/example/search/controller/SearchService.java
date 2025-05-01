package com.example.search.service;

import com.example.search.model.BookResponse;
import com.example.search.model.AuthorResponse;
import com.example.search.model.SearchResultResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class SearchService {

    @Autowired
    private RestTemplate restTemplate;

    public SearchResultResponse search(String query) {
        try {
            // Create CompletableFuture for book and author service calls
            CompletableFuture<List<BookResponse>> booksFuture = getBooksAsync(query);
            CompletableFuture<List<AuthorResponse>> authorsFuture = getAuthorsAsync(query);
            CompletableFuture<String> detailsFuture = getServiceDetailsAsync();

            // Wait for all futures to complete
            CompletableFuture.allOf(booksFuture, authorsFuture, detailsFuture).join();

            // Build the search result response
            SearchResultResponse response = new SearchResultResponse();
            response.setBooks(booksFuture.get());
            response.setAuthors(authorsFuture.get());
            response.setServiceDetails(detailsFuture.get());

            return response;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error processing search request", e);
        }
    }

    // Async method to get books
    private CompletableFuture<List<BookResponse>> getBooksAsync(String query) {
        return CompletableFuture.supplyAsync(() -> getBooks(query));
    }

    // Hystrix protected method to get books
    @HystrixCommand(fallbackMethod = "getBooksFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    private List<BookResponse> getBooks(String query) {
        // Call book service
        BookResponse[] books = restTemplate.getForObject(
                "http://book-service/books/search?query=" + query,
                BookResponse[].class
        );
        return books != null ? Arrays.asList(books) : Collections.emptyList();
    }

    // Fallback method for getBooks
    private List<BookResponse> getBooksFallback(String query) {
        // Return empty list as fallback
        return Collections.emptyList();
    }

    // Async method to get authors
    private CompletableFuture<List<AuthorResponse>> getAuthorsAsync(String query) {
        return CompletableFuture.supplyAsync(() -> getAuthors(query));
    }

    // Hystrix protected method to get authors
    @HystrixCommand(fallbackMethod = "getAuthorsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    private List<AuthorResponse> getAuthors(String query) {
        // Call author service
        AuthorResponse[] authors = restTemplate.getForObject(
                "http://author-service/authors/search?query=" + query,
                AuthorResponse[].class
        );
        return authors != null ? Arrays.asList(authors) : Collections.emptyList();
    }

    // Fallback method for getAuthors
    private List<AuthorResponse> getAuthorsFallback(String query) {
        // Return empty list as fallback
        return Collections.emptyList();
    }

    // Async method to get service details
    private CompletableFuture<String> getServiceDetailsAsync() {
        return CompletableFuture.supplyAsync(() -> getServiceDetails());
    }

    // Hystrix protected method to get service details
    @HystrixCommand(fallbackMethod = "getServiceDetailsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    private String getServiceDetails() {
        // Call details service
        return restTemplate.getForObject("http://details-service/details/port", String.class);
    }

    // Fallback method for getServiceDetails
    private String getServiceDetailsFallback() {
        // Return a default value as fallback
        return "Details service unavailable";
    }
}