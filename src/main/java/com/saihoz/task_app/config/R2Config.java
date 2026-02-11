package com.saihoz.task_app.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class R2Config {
    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.endpoint}")
    private URI endpoint;

    @Bean
    public S3Client s3Client() {

        return S3Client.builder()
                .endpointOverride(endpoint)
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.US_EAST_1) // R2 ignora la región, pero el SDK la pide (usa US_EAST_1 o AUTO)
                .endpointOverride(endpoint)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}
