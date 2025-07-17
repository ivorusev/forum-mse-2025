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
public interface SearchSuggestionRepository extends JpaRepository<SearchSuggestion, Long> {

    List<SearchSuggestion> findByTextContainingIgnoreCaseOrderByPopularityDesc(String text, Pageable pageable);

    @Query("SELECT ss FROM SearchSuggestion ss WHERE " +
            "LOWER(ss.text) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY ss.popularity DESC, ss.text ASC")
    List<SearchSuggestion> findSuggestionsByQuery(@Param("query") String query, Pageable pageable);
}