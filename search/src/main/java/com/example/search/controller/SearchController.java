package com.example.search.controller;

import com.example.search.model.ApiResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final RestTemplate restTemplate;

    // Constructor injection
    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    @HystrixCommand(fallbackMethod = "fallbackSearch")
    public ApiResponse<Map<String, Object>> search(@PathVariable String id) {
        CompletableFuture<Object> bookFut = CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject("http://book-service/books/" + id, Object.class)
        );
        CompletableFuture<Object> detailsFut = CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject("http://details-service/details/" + id, Object.class)
        );

        // Wait for both to finish
        CompletableFuture.allOf(bookFut, detailsFut).join();

        // Build a mutable Map (Java 8 compatible)
        Map<String,Object> payload = new HashMap<>();
        payload.put("book",    bookFut.join());
        payload.put("details", detailsFut.join());

        return new ApiResponse<>(200, Instant.now(), payload);
    }

    // Fallback must match (String) signature
    public ApiResponse<String> fallbackSearch(String id) {
        String msg = "Service temporarily unavailable for id=" + id;
        return new ApiResponse<>(503, Instant.now(), msg);
    }
}
