package com.edu.pwc.forum.api;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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

@RestController
@RequestMapping("/api/search")
//@CrossOrigin(origins = "http://localhost:3000")
//@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/suggestions")
    public ResponseEntity<SearchSuggestionsResponse> getSuggestions(
            @RequestParam("q") String query,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        try {
            List<SearchSuggestion> suggestions = searchService.getSuggestions(query, limit);
            return ResponseEntity.ok(new SearchSuggestionsResponse(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SearchSuggestionsResponse(Collections.emptyList()));
        }
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        try {
            SearchResponse response = searchService.search(query, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SearchResponse(Collections.emptyList(), 0, 0));
        }
    }

    @PostMapping("/recent")
    public ResponseEntity<Void> addRecentSearch(
            @RequestBody RecentSearchRequest request,
            HttpServletRequest httpRequest) {

        try {
            String userIdentifier = getUserIdentifier(httpRequest);
            searchService.addRecentSearch(userIdentifier, request.getQuery());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<String>> getRecentSearches(HttpServletRequest request) {
        try {
            String userIdentifier = getUserIdentifier(request);
            List<String> recentSearches = searchService.getRecentSearches(userIdentifier);
            return ResponseEntity.ok(recentSearches);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    private String getUserIdentifier(HttpServletRequest request) {
        // Simple implementation - in production, use proper user authentication
        String sessionId = request.getSession().getId();
        return sessionId != null ? sessionId : "anonymous";
    }
}
