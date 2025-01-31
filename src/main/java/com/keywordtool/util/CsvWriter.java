package com.keywordtool.util;

import com.keywordtool.model.KeywordSuggestion;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvWriter {
    private static final String[] HEADERS = {
        "Aranan Kelime",
        "Önerilen Anahtar Kelime",
        "Aylık Ortalama Arama",
        "Rekabet",
        "Önerilen Teklif"
    };

    public void writeToCSV(String searchedKeyword, List<KeywordSuggestion> suggestions, String outputDirectory) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%s/keyword_suggestions_%s_%s.csv", 
            outputDirectory, 
            searchedKeyword.replaceAll("\\s+", "_"), 
            timestamp);

        try (FileWriter writer = new FileWriter(fileName)) {
            // UTF-8 BOM ekle
            writer.write('\ufeff');
            
            // Başlıkları yaz
            writer.write(String.join(",", HEADERS) + "\n");

            // Verileri yaz
            for (KeywordSuggestion suggestion : suggestions) {
                String line = String.format("\"%s\",\"%s\",%d,\"%s\",%.2f\n",
                    searchedKeyword,
                    suggestion.getKeyword(),
                    suggestion.getAverageMonthlySearches() != null ? suggestion.getAverageMonthlySearches() : 0, // Null kontrolü
                    suggestion.getCompetition() != null ? suggestion.getCompetition() : "N/A", // Null kontrolü
                    suggestion.getSuggestedBid() != null ? suggestion.getSuggestedBid() : 0.0); // Null kontrolü
                writer.write(line);
            }
        }
        System.out.println("CSV dosyası oluşturuldu: " + fileName);
    }
    
}