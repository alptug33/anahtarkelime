package com.keywordtool.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonReader {
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON işlemleri için ObjectMapper

    public List<String> readKeywords(String filename) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Dosya bulunamadı: " + filename); // Dosya bulunamadığında hata fırlat
            }
            // JSON dosyasını oku ve String listesine dönüştür
            return objectMapper.readValue(input,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        }
    }
}