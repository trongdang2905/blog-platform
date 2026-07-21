package com.group2.blogplatform.config;

import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageKitConfig {

    @Value("${app.imagekit.private:dummy_key}")
    private String privateKey;

    @Bean
    public ImageKitClient imageKitClient() {
        ImageKitClient client = ImageKitOkHttpClient.builder()
                .privateKey(privateKey)
                .build();
        return client;
    }
}