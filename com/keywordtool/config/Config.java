private String accessToken;

private void loadConfig() {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
        if (input == null) {
            throw new RuntimeException("application.properties dosyası bulunamadı!");
        }

        Properties prop = new Properties();
        prop.load(input);

        this.accessToken = prop.getProperty("google.ads.accessToken");
    } catch (IOException ex) {
        throw new RuntimeException("Konfigürasyon dosyası okunamadı: " + ex.getMessage(), ex);
    }
} 