package com.example.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailsResponse {
    private String port;
    private String additionalInfo;
    // Add more fields as needed
}
