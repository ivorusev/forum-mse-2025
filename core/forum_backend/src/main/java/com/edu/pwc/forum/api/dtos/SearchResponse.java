package com.edu.pwc.forum.api.dtos;

import lombok.Data;
import java.util.List;
import com.edu.pwc.forum.persistence.entity.SearchResult;
import com.edu.pwc.forum.persistence.entity.SearchSuggestion;

public class SearchResponse {
    private List<SearchResult> results;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;

    public SearchResponse() {}

    public SearchResponse(List<SearchResult> results, long totalElements, int totalPages) {
        this.results = results;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    // Getters and setters
    public List<SearchResult> getResults() { return results; }
    public void setResults(List<SearchResult> results) { this.results = results; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}