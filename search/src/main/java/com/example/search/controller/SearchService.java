package com.example.search.controller;

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

    @HystrixCommand(fallbackMethod = "fallbackSearch")
    public SearchResultResponse search(String query) {
        try {
            CompletableFuture<List<BookResponse>> booksFuture    = getBooksAsync(query);
            CompletableFuture<List<AuthorResponse>> authorsFuture = getAuthorsAsync(query);
            CompletableFuture<String> detailsFuture               = getServiceDetailsAsync();

            CompletableFuture.allOf(booksFuture, authorsFuture, detailsFuture).join();

            SearchResultResponse resp = new SearchResultResponse();
            resp.setBooks(booksFuture.get());
            resp.setAuthors(authorsFuture.get());
            resp.setServiceDetails(detailsFuture.get());

            return resp;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error processing search request", e);
        }
    }

    // Async wrappers
    private CompletableFuture<String> getServiceDetailsAsync() {
        return CompletableFuture.supplyAsync(this::getServiceDetails);
    }

    private CompletableFuture<List<AuthorResponse>> getAuthorsAsync(String query) {
        return CompletableFuture.supplyAsync(() -> getAuthors(query));
    }

    private CompletableFuture<List<BookResponse>> getBooksAsync(String query) {
        return CompletableFuture.supplyAsync(() -> getBooks(query));
    }

    // Synchronous, Hystrix-protected calls
    @HystrixCommand(fallbackMethod = "getServiceDetailsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",    value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",  value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    private String getServiceDetails() {
        return restTemplate.getForObject(
                "http://details-service/details/port",
                String.class
        );
    }

    private String getServiceDetailsFallback() {
        return "Details service unavailable";
    }

    @HystrixCommand(fallbackMethod = "getAuthorsFallback")
    private List<AuthorResponse> getAuthors(String query) {
        AuthorResponse[] array = restTemplate.getForObject(
                "http://author-service/authors/search?query=" + query,
                AuthorResponse[].class
        );
        return array != null ? Arrays.asList(array) : Collections.emptyList();
    }

    private List<AuthorResponse> getAuthorsFallback(String query) {
        return Collections.emptyList();
    }

    @HystrixCommand(fallbackMethod = "getBooksFallback")
    private List<BookResponse> getBooks(String query) {
        BookResponse[] array = restTemplate.getForObject(
                "http://book-service/books/search?query=" + query,
                BookResponse[].class
        );
        return array != null ? Arrays.asList(array) : Collections.emptyList();
    }

    private List<BookResponse> getBooksFallback(String query) {
        return Collections.emptyList();
    }

    // Fallback for the overall search
    public SearchResultResponse fallbackSearch(String query) {
        SearchResultResponse fallback = new SearchResultResponse();
        fallback.setBooks(Collections.emptyList());
        fallback.setAuthors(Collections.emptyList());
        fallback.setServiceDetails("Service temporarily unavailable");
        return fallback;
    }
}
