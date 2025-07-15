import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Search, X, Clock, TrendingUp } from 'lucide-react';

interface SearchResult {
  id: string;
  title: string;
  description: string;
  category: string;
  relevanceScore: number;
}

interface SearchSuggestion {
  id: string;
  text: string;
  category: string;
  isPopular?: boolean;
}

const SearchPage: React.FC = () => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState<SearchSuggestion[]>([]);
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1);
  const [recentSearches, setRecentSearches] = useState<string[]>([]);

  const searchInputRef = useRef<HTMLInputElement>(null);
  const suggestionsRef = useRef<HTMLDivElement>(null);
  const debounceTimerRef = useRef<NodeJS.Timeout>(null);

  // Mock API base URL - replace with your actual backend URL
  const API_BASE_URL = 'http://localhost:8080/api';

  // Debounced search function
  const debouncedSearch = useCallback((searchQuery: string) => {
    if (debounceTimerRef.current) {
      clearTimeout(debounceTimerRef.current);
    }

    debounceTimerRef.current = setTimeout(() => {
      if (searchQuery.trim().length > 0) {
        fetchSuggestions(searchQuery);
      } else {
        setSuggestions([]);
        setShowSuggestions(false);
      }
    }, 300);
  }, []);

  // Fetch suggestions from backend
  const fetchSuggestions = async (searchQuery: string) => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE_URL}/search/suggestions?q=${encodeURIComponent(searchQuery)}`);
      const data = await response.json();
      setSuggestions(data.suggestions || []);
      setShowSuggestions(true);
    } catch (error) {
      console.error('Error fetching suggestions:', error);
      // Fallback to mock data for demo
      const mockSuggestions = [
        { id: '1', text: `${searchQuery} topics`, category: 'Topics' },
        { id: '2', text: `${searchQuery} replies`, category: 'Replies' },
        { id: '3', text: `${searchQuery} users`, category: 'Users' }
      ];
      setSuggestions(mockSuggestions);
      setShowSuggestions(true);
    } finally {
      setIsLoading(false);
    }
  };

  // Perform full search
  const performSearch = async (searchQuery: string) => {
    if (!searchQuery.trim()) return;

    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE_URL}/search?q=${encodeURIComponent(searchQuery)}`);
      const data = await response.json();
      setSearchResults(data.results || []);

      // Add to recent searches
      setRecentSearches(prev => {
        const updated = [searchQuery, ...prev.filter(s => s !== searchQuery)];
        return updated.slice(0, 5);
      });

      setShowSuggestions(false);
    } catch (error) {
      console.error('Error performing search:', error);
      // Fallback to mock data for demo
      const mockResults = [
        {
          id: '1',
          title: `How to implement ${searchQuery}`,
          description: `A comprehensive topics on implementing ${searchQuery} with best practices and examples.`,
          category: 'Topics',
          relevanceScore: 0.95
        },
        {
          id: '2',
          title: `${searchQuery} - Complete reference on all replies`,
          description: `Everything you need to know about ${searchQuery}, including advanced techniques and troubleshooting.`,
          category: 'Replies',
          relevanceScore: 0.87
        },
        {
          id: '3',
          title: `Common ${searchQuery} mistakes to avoid`,
          description: `Learn about the most common pitfalls when working with ${searchQuery} and how to avoid them.`,
          category: 'Users',
          relevanceScore: 0.78
        }
      ];
      setSearchResults(mockResults);
      setShowSuggestions(false);
    } finally {
      setIsLoading(false);
    }
  };

  // Handle input change
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setQuery(value);
    setSelectedSuggestionIndex(-1);
    debouncedSearch(value);
  };

  // Handle key navigation
  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!showSuggestions) return;

    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        setSelectedSuggestionIndex(prev =>
          prev < suggestions.length - 1 ? prev + 1 : prev
        );
        break;
      case 'ArrowUp':
        e.preventDefault();
        setSelectedSuggestionIndex(prev => prev > 0 ? prev - 1 : -1);
        break;
      case 'Enter':
        e.preventDefault();
        if (selectedSuggestionIndex >= 0) {
          const selectedSuggestion = suggestions[selectedSuggestionIndex];
          setQuery(selectedSuggestion.text);
          performSearch(selectedSuggestion.text);
        } else {
          performSearch(query);
        }
        break;
      case 'Escape':
        setShowSuggestions(false);
        setSelectedSuggestionIndex(-1);
        break;
    }
  };

  // Handle suggestion click
  const handleSuggestionClick = (suggestion: SearchSuggestion) => {
    setQuery(suggestion.text);
    performSearch(suggestion.text);
  };

  // Handle search submit
  /*const handleSearchSubmit = () => {
    performSearch(query);
  };*/

  // Clear search
  const clearSearch = () => {
    setQuery('');
    setSearchResults([]);
    setSuggestions([]);
    setShowSuggestions(false);
    searchInputRef.current?.focus();
  };

  // Click outside handler
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (suggestionsRef.current && !suggestionsRef.current.contains(event.target as Node)) {
        setShowSuggestions(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="max-w-4xl mx-auto p-5 bg-gray-50 min-h-screen">
      <div className="bg-white rounded-lg shadow-lg p-5 mb-5">
        <h1 className="text-3xl font-bold text-gray-800 mb-5 text-center">
          Search with autocomplete
        </h1>

        {/* Search Input */}
        <div className="relative mb-5">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              ref={searchInputRef}
              type="text"
              value={query}
              onChange={handleInputChange}
              onKeyDown={handleKeyDown}
              onFocus={() => query && setShowSuggestions(true)}
              placeholder="Search for topics, replies, users..."
              className="w-full pl-10 pr-10 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-lg"
            />
            {query && (
              <button
                type="button"
                onClick={clearSearch}
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
              >
                <X className="w-5 h-5" />
              </button>
            )}
          </div>

          {/* Suggestions Dropdown */}
          {showSuggestions && (suggestions.length > 0 || isLoading) && (
            <div
              ref={suggestionsRef}
              className="absolute z-10 w-full bg-white border border-gray-200 rounded-lg mt-1 shadow-lg max-h-80 overflow-y-auto"
            >
              {isLoading && (
                <div className="p-4 text-center text-gray-500">
                  <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-500 mx-auto"></div>
                  <span className="ml-2">Searching...</span>
                </div>
              )}

              {!isLoading && suggestions.map((suggestion, index) => (
                <div
                  key={suggestion.id}
                  onClick={() => handleSuggestionClick(suggestion)}
                  className={`p-3 cursor-pointer border-b border-gray-100 last:border-b-0 hover:bg-gray-50 ${
                    index === selectedSuggestionIndex ? 'bg-blue-50' : ''
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      {suggestion.isPopular ? (
                        <TrendingUp className="w-4 h-4 text-orange-500" />
                      ) : (
                        <Search className="w-4 h-4 text-gray-400" />
                      )}
                      <span className="text-gray-800">{suggestion.text}</span>
                    </div>
                    <span className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
                      {suggestion.category}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Recent Searches */}
        {recentSearches.length > 0 && !showSuggestions && !searchResults.length && (
          <div className="mb-5">
            <h3 className="text-lg font-semibold text-gray-700 mb-3 flex items-center">
              <Clock className="w-5 h-5 mr-2" />
              Recent Searches
            </h3>
            <div className="flex flex-wrap gap-2">
              {recentSearches.map((search, index) => (
                <button
                  key={index}
                  onClick={() => {
                    setQuery(search);
                    performSearch(search);
                  }}
                  className="px-3 py-1 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300 transition-colors"
                >
                  {search}
                </button>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Search Results */}
      {searchResults.length > 0 && (
        <div className="bg-white rounded-lg shadow-lg p-5">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">
            Search Results ({searchResults.length})
          </h2>
          <div className="space-y-4">
            {searchResults.map((result) => (
              <div
                key={result.id}
                className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
              >
                <div className="flex items-start justify-between mb-2">
                  <h3 className="text-lg font-semibold text-blue-600 hover:text-blue-800 cursor-pointer">
                    {result.title}
                  </h3>
                  <div className="flex items-center space-x-2">
                    <span className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
                      {result.category}
                    </span>
                    <span className="text-xs text-green-600 font-medium">
                      {Math.round(result.relevanceScore * 100)}% match
                    </span>
                  </div>
                </div>
                <p className="text-gray-600 text-sm leading-relaxed">
                  {result.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* No Results */}
      {query && !isLoading && searchResults.length === 0 && !showSuggestions && (
        <div className="bg-white rounded-lg shadow-lg p-5 text-center">
          <div className="text-gray-500 mb-4">
            <Search className="w-12 h-12 mx-auto mb-2 opacity-50" />
            <p className="text-lg">No results found for "{query}"</p>
            <p className="text-sm">Try different keywords or check your spelling</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default SearchPage;