package com.edu.pwc.forum.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RecentSearchRequest {
    private String query;

    public RecentSearchRequest() {}

    public RecentSearchRequest(String query) {
        this.query = query;
    }

    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}
