package com.keywordtool.service;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v18.enums.KeywordPlanNetworkEnum.KeywordPlanNetwork;
import com.google.ads.googleads.v18.errors.GoogleAdsException;
import com.google.ads.googleads.v18.services.GenerateKeywordIdeaResult;
import com.google.ads.googleads.v18.services.GenerateKeywordIdeasRequest;
import com.google.ads.googleads.v18.services.KeywordPlanIdeaServiceClient;
import com.google.ads.googleads.v18.services.KeywordSeed;
import com.google.ads.googleads.v18.utils.ResourceNames;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class GoogleAdsService {
    private final GoogleAdsClient googleAdsClient;
    private final long customerId;
    private final long languageId;
    private final long locationId;

    public GoogleAdsService(long customerId, long languageId, long locationId, String accessToken) throws IOException {
        // GoogleCredentials kullanarak kimlik doğrulama
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));

        this.googleAdsClient = GoogleAdsClient.newBuilder()
                .fromPropertiesFile() // Properties dosyasından ayarları yükle
                .setCredentials(credentials) // GoogleCredentials ile kimlik doğrulama
                .build();
        this.customerId = customerId;
        this.languageId = languageId;
        this.locationId = locationId;
    }

    public List<String> getKeywordSuggestions(List<String> keywords) {
        try (KeywordPlanIdeaServiceClient client = 
                googleAdsClient.getLatestVersion().createKeywordPlanIdeaServiceClient()) {

            // KeywordSeed nesnesi oluşturuluyor
            KeywordSeed keywordSeed = KeywordSeed.newBuilder()
                    .addAllKeywords(keywords)
                    .build();

            // GenerateKeywordIdeasRequest oluşturuluyor
            GenerateKeywordIdeasRequest request = GenerateKeywordIdeasRequest.newBuilder()
                    .setCustomerId(String.valueOf(customerId))
                    .setLanguage(ResourceNames.languageConstant(languageId))
                    .addGeoTargetConstants(ResourceNames.geoTargetConstant(locationId))
                    .setKeywordPlanNetwork(KeywordPlanNetwork.GOOGLE_SEARCH)
                    .setKeywordSeed(keywordSeed) // KeywordSeed'i ekle
                    .build();

            // Anahtar kelime önerilerini döndür
            return StreamSupport.stream(client.generateKeywordIdeas(request).iterateAll().spliterator(), false)
                    .map(GenerateKeywordIdeaResult::getText)
                    .collect(Collectors.toList());

        } catch (GoogleAdsException e) {
            log.error("Google Ads API Error: {}", e.getGoogleAdsFailure().getErrorsList());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
        }
        return Collections.emptyList(); // Hata durumunda boş liste döndür
    }
}