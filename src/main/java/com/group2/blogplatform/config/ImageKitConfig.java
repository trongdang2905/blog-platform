package com.group2.blogplatform.config;

import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageKitConfig {
    @Bean
    public ImageKitClient imageKitClient() {
        ImageKitClient client = ImageKitOkHttpClient.builder()
                .privateKey(System.getenv("app_imagekit_private"))
                .build();
        return client;
    }
}
