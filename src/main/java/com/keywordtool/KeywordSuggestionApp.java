package com.keywordtool;

import com.keywordtool.service.GoogleAdsService;
import com.keywordtool.config.Config;
import com.keywordtool.util.JsonReader;

import java.util.Collections;
import java.util.List;

public class KeywordSuggestionApp {
    private static final long CUSTOMER_ID = 7220127343L; // CUSTOMER_ID'yi long olarak tanımlayın

    public static void main(String[] args) {
    	
        try {
            System.out.println("Google Ads Anahtar Kelime Aracı başlatılıyor...");
            
            // Konfigürasyonu yükle
            Config config = new Config();
            config.loadConfig();
            
            // Google Ads servisi başlatma
            GoogleAdsService adsService = new GoogleAdsService(CUSTOMER_ID, 1000L, 2000L, config.getAccessToken());
            
            // JSON dosyasından kelimeleri okuma
            JsonReader jsonReader = new JsonReader();
            List<String> keywords = jsonReader.readKeywords("keywords.json");
            
            // Her kelime için öneri al ve yazdır
            for (String keyword : keywords) {
                System.out.println("\nAranan kelime: " + keyword);
                
                List<String> suggestions = adsService.getKeywordSuggestions(Collections.singletonList(keyword));
                
                System.out.println("Önerilen kelimeler:");
                suggestions.forEach(suggestion -> System.out.println("- " + suggestion));
            }
            
        } catch (Exception e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}