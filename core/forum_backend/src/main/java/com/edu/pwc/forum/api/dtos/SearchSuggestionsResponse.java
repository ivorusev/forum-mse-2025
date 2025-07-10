package com.edu.pwc.forum.api.dtos;

import java.util.List;
import lombok.Data;
import com.edu.pwc.forum.persistence.entity.SearchResult;
import com.edu.pwc.forum.persistence.entity.SearchSuggestion;

public class SearchSuggestionsResponse {
    private List<SearchSuggestion> suggestions;

    public SearchSuggestionsResponse() {}

    public SearchSuggestionsResponse(List<SearchSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    // Getters and setters
    public List<SearchSuggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<SearchSuggestion> suggestions) { this.suggestions = suggestions; }
}
