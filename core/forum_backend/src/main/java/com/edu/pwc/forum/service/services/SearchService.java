package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.RecentSearchRequest;
import com.edu.pwc.forum.api.dtos.SearchResponse;
import com.edu.pwc.forum.api.dtos.SearchSuggestionsResponse;
import com.edu.pwc.forum.service.services.SearchService;
import com.edu.pwc.forum.persistence.entity.RecentSearch;
import com.edu.pwc.forum.persistence.entity.SearchAnalytics;
import com.edu.pwc.forum.persistence.entity.SearchResult;
import com.edu.pwc.forum.persistence.entity.SearchSuggestion;
import com.edu.pwc.forum.persistence.repositories.RecentSearchRepository;
import com.edu.pwc.forum.persistence.repositories.SearchRepository;
import com.edu.pwc.forum.persistence.repositories.SearchSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchSuggestionRepository suggestionRepository;

    @Autowired
    private RecentSearchRepository recentSearchRepository;

    private static final int MAX_RECENT_SEARCHES = 10;

    public List<SearchSuggestion> getSuggestions(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedQuery = query.toLowerCase().trim();

        // Get suggestions from database
        List<SearchSuggestion> suggestions = suggestionRepository
                .findByTextContainingIgnoreCaseOrderByPopularityDesc(normalizedQuery, PageRequest.of(0, limit));

        // If not enough suggestions, generate some based on popular patterns
        if (suggestions.size() < limit) {
            suggestions.addAll(generateAdditionalSuggestions(normalizedQuery, limit - suggestions.size()));
        }

        return suggestions.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public SearchResponse search(String query, int page, int size) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResponse(Collections.emptyList(), 0, 0);
        }

        // Record the search for analytics
        recordSearch(query);

        // Perform the actual search
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchResult> results = searchRepository.findByQuery(query, pageable);

        return new SearchResponse(
                results.getContent(),
                results.getTotalElements(),
                results.getTotalPages()
        );
    }

    public void addRecentSearch(String userIdentifier, String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }

        // Remove existing entry if present
        recentSearchRepository.deleteByUserIdentifierAndQuery(userIdentifier, query);

        // Add new entry
        RecentSearch recentSearch = new RecentSearch();
        recentSearch.setUserIdentifier(userIdentifier);
        recentSearch.setQuery(query);
        recentSearch.setSearchTime(LocalDateTime.now());
        recentSearchRepository.save(recentSearch);

        // Keep only the most recent searches
        List<RecentSearch> allRecent = recentSearchRepository
                .findByUserIdentifierOrderBySearchTimeDesc(userIdentifier);

        if (allRecent.size() > MAX_RECENT_SEARCHES) {
            List<RecentSearch> toDelete = allRecent.subList(MAX_RECENT_SEARCHES, allRecent.size());
            recentSearchRepository.deleteAll(toDelete);
        }
    }

    public List<String> getRecentSearches(String userIdentifier) {
        return recentSearchRepository
                .findByUserIdentifierOrderBySearchTimeDesc(userIdentifier)
                .stream()
                .map(RecentSearch::getQuery)
                .collect(Collectors.toList());
    }

    private void recordSearch(String query) {
        // Record search analytics
        SearchAnalytics analytics = new SearchAnalytics();
        analytics.setQuery(query);
        analytics.setSearchTime(LocalDateTime.now());
        analytics.setResultCount(0); // You can update this with actual result count

        // Save asynchronously to avoid blocking the search
        CompletableFuture.runAsync(() -> {
            try {
                // Save to analytics table
                searchRepository.saveSearchAnalytics(analytics);
            } catch (Exception e) {
                // Log error but don't fail the search
                System.err.println("Failed to record search analytics: " + e.getMessage());
            }
        });
    }

    private List<SearchSuggestion> generateAdditionalSuggestions(String query, int count) {
        List<SearchSuggestion> additional = new ArrayList<>();
        String[] patterns = {
                " tutorial", " guide", " example", " best practices",
                " how to", " tips", " documentation", " reference"
        };

        for (int i = 0; i < Math.min(count, patterns.length); i++) {
            SearchSuggestion suggestion = new SearchSuggestion();
            suggestion.setText(query + patterns[i]);
            suggestion.setCategory(getCategoryForPattern(patterns[i]));
            suggestion.setPopularity(50 - i * 5); // Decreasing popularity
            additional.add(suggestion);
        }

        return additional;
    }

    private String getCategoryForPattern(String pattern) {
        switch (pattern.trim()) {
            case "tutorial": return "Tutorial";
            case "guide": return "Guide";
            case "example": return "Example";
            case "best practices": return "Best Practices";
            case "how to": return "How To";
            case "tips": return "Tips";
            case "documentation": return "Documentation";
            case "reference": return "Reference";
            default: return "General";
        }
    }
}