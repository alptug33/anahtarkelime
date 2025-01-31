package com.keywordtool.model;

import lombok.Data;

@Data
public class KeywordSuggestion {
    private String keyword;
    private Long averageMonthlySearches; // Aylık ortalama arama
    private String competition; // Rekabet durumu
    private Double suggestedBid; 
    
}