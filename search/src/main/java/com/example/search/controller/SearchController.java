package com.example.search.controller;

import com.example.common.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public GeneralResponse<SearchResultResponse> search(@RequestParam String query) {
        SearchResultResponse result = searchService.search(query);
        return new GeneralResponse<>(200, "Success", result);
    }
}