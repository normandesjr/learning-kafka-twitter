package com.hibicode.kafkatwitterproducer.config;

import com.hibicode.kafkatwitterproducer.security.KeyDecrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class TwitterCredentialsConfig {

    @Bean
    public TwitterCredential create(KeyDecrypt keyDecrypt) {
        return new TwitterCredential(keyDecrypt.decrypt());
    }

    public static class TwitterCredential {

        private final String apiKey;
        private final String apiSecretKey;
        private final String accessToken;
        private final String accessTokenSecret;

        public TwitterCredential(String string) {
            final String[] values = string.split(",");
            this.apiKey = values[0].replaceAll("api_key=", "");
            this.apiSecretKey = values[1].replaceAll("api_secret_key=", "");
            this.accessToken = values[2].replaceAll("access_token=", "");
            this.accessTokenSecret = values[3].replaceAll("access_token_secret=", "");
        }

        public TwitterCredential(String apiKey, String apiSecretKey, String accessToken, String accessTokenSecret) {
            this.apiKey = apiKey;
            this.apiSecretKey = apiSecretKey;
            this.accessToken = accessToken;
            this.accessTokenSecret = accessTokenSecret;
        }

        public String getApiKey() {
            return apiKey;
        }

        public String getApiSecretKey() {
            return apiSecretKey;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getAccessTokenSecret() {
            return accessTokenSecret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TwitterCredential that = (TwitterCredential) o;
            return Objects.equals(apiKey, that.apiKey) &&
                    Objects.equals(apiSecretKey, that.apiSecretKey) &&
                    Objects.equals(accessToken, that.accessToken) &&
                    Objects.equals(accessTokenSecret, that.accessTokenSecret);
        }

        @Override
        public int hashCode() {
            return Objects.hash(apiKey, apiSecretKey, accessToken, accessTokenSecret);
        }

        @Override
        public String toString() {
            return "TwitterCredential{" +
                    "apiKey='" + apiKey + '\'' +
                    ", apiSecretKey='" + apiSecretKey + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", accessTokenSecret='" + accessTokenSecret + '\'' +
                    '}';
        }
    }

}
