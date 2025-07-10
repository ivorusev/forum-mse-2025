package com.edu.pwc.forum.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "search_suggestions")
public class SearchSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String text;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer popularity;

    @Column(name = "is_popular")
    private Boolean isPopular;

    // Constructors, getters, and setters
    public SearchSuggestion() {}

    public SearchSuggestion(String text, String category, Integer popularity) {
        this.text = text;
        this.category = category;
        this.popularity = popularity;
        this.isPopular = popularity > 80;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getPopularity() { return popularity; }
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
        this.isPopular = popularity > 80;
    }

    public Boolean getIsPopular() { return isPopular; }
    public void setIsPopular(Boolean isPopular) { this.isPopular = isPopular; }
}