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
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

    List<RecentSearch> findByUserIdentifierOrderBySearchTimeDesc(String userIdentifier);

    void deleteByUserIdentifierAndQuery(String userIdentifier, String query);

    @Query("SELECT rs FROM RecentSearch rs WHERE rs.userIdentifier = :userIdentifier ORDER BY rs.searchTime DESC")
    List<RecentSearch> findRecentSearches(@Param("userIdentifier") String userIdentifier, Pageable pageable);
}