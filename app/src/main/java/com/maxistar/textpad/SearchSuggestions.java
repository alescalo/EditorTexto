package com.maxistar.textpad;

import android.content.SearchRecentSuggestionsProvider;
/**
 * Proporciona una sugerencia de búsquedas al editor
 */
public class SearchSuggestions extends SearchRecentSuggestionsProvider {
    /**
     * Autoridad
     */
    public final static String AUTHORITY = "com.maxistar.authority";
    /**
     * Modo
     */
    public final static int MODE = DATABASE_MODE_QUERIES;
    /**
     * Constructor
     */
    public SearchSuggestions() {
        setupSuggestions(AUTHORITY, MODE);
    }
}