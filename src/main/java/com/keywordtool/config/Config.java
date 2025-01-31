package com.keywordtool.config;

import lombok.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class Config {
    private String developerToken;
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private long customerId;
    private String accessToken;

    public Config() {
        loadConfig();
    }

    public void loadConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("ads.properties")) {
            if (input == null) {
                throw new RuntimeException("ads.properties dosyası bulunamadı!"); // Dosya adı güncellendi
            }

            Properties prop = new Properties();
            prop.load(input);

            this.developerToken = prop.getProperty("google.ads.developerToken");
            this.clientId = prop.getProperty("google.ads.clientId");
            this.clientSecret = prop.getProperty("google.ads.clientSecret");
            this.refreshToken = prop.getProperty("google.ads.refreshToken");
            this.customerId = Long.parseLong(prop.getProperty("google.ads.customerId", "0"));
            this.accessToken = prop.getProperty("google.ads.accessToken"); // accessToken burada yükleniyor
        } catch (IOException ex) {
            throw new RuntimeException("Konfigürasyon dosyası okunamadı: " + ex.getMessage(), ex);
        }
    }

    // Google Ads API için properties dosyası oluştur
    public void createGoogleAdsProperties() {
        Properties googleAdsProp = new Properties();
        googleAdsProp.setProperty("api.googleads.developerToken", developerToken);
        googleAdsProp.setProperty("api.googleads.clientId", clientId);
        googleAdsProp.setProperty("api.googleads.clientSecret", clientSecret);
        googleAdsProp.setProperty("api.googleads.refreshToken", refreshToken);
        googleAdsProp.setProperty("api.googleads.loginCustomerId", String.valueOf(customerId));

        try (java.io.FileWriter writer = new java.io.FileWriter("google-ads.properties")) {
            googleAdsProp.store(writer, "Google Ads API Configuration");
        } catch (IOException ex) {
            throw new RuntimeException("google-ads.properties dosyası oluşturulamadı: " + ex.getMessage());
        }
    }
}