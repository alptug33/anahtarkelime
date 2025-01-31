@Slf4j
public class GoogleAdsService {
    private final Long customerId;
    private final long languageId;
    private final long locationId;
    private final GoogleAdsClient googleAdsClient;

    public GoogleAdsService(Long customerId, long languageId, long locationId, String accessToken) throws IOException {
        this.googleAdsClient = GoogleAdsClient.newBuilder()
            .fromPropertiesFile()
            .setOAuth2Credentials(new AccessToken(accessToken))
            .build();
        this.customerId = customerId;
        this.languageId = languageId;
        this.locationId = locationId;
    }

    public List<String> getKeywordSuggestions(List<String> keywords) {
        try (KeywordPlanIdeaServiceClient client = 
                googleAdsClient.getLatestVersion().createKeywordPlanIdeaServiceClient()) {

            KeywordSeed keywordSeed = KeywordSeed.newBuilder()
                    .addAllKeywords(keywords)
                    .build();

            GenerateKeywordIdeasRequest request = GenerateKeywordIdeasRequest.newBuilder()
                    .setCustomerId(String.valueOf(customerId))
                    .setLanguage(ResourceNames.languageConstant(languageId))
                    .addGeoTargetConstants(ResourceNames.geoTargetConstant(locationId))
                    .setKeywordPlanNetwork(KeywordPlanNetwork.GOOGLE_SEARCH)
                    .setKeywordSeed(keywordSeed)
                    .build();

            return StreamSupport.stream(client.generateKeywordIdeas(request).iterateAll().spliterator(), false)
                    .map(GenerateKeywordIdeaResult::getText)
                    .collect(Collectors.toList());

        } catch (GoogleAdsException e) {
            log.error("Google Ads API Error: {}", e.getGoogleAdsFailure().getErrorsList());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
        }
        return Collections.emptyList();
    }
} 