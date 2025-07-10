package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.RecentSearch;
import com.edu.pwc.forum.persistence.entity.SearchAnalytics;
import com.edu.pwc.forum.persistence.entity.SearchResult;
import com.edu.pwc.forum.persistence.entity.SearchSuggestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<SearchResult, Long> {

    @Query("SELECT sr FROM SearchResult sr WHERE " +
            "LOWER(sr.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(sr.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(sr.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY sr.relevanceScore DESC")
    Page<SearchResult> findByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT sr FROM SearchResult sr WHERE " +
            "LOWER(sr.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(sr.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY sr.relevanceScore DESC")
    List<SearchResult> findByQueryForSuggestions(@Param("query") String query, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO search_analytics (query, search_time, result_count) VALUES (:#{#analytics.query}, :#{#analytics.searchTime}, :#{#analytics.resultCount})", nativeQuery = true)
    void saveSearchAnalytics(@Param("analytics") SearchAnalytics analytics);
}