package com.saihoz.task_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class StorageService {

    @Autowired
    private S3Presigner s3Presigner;

    @Autowired
    private S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    public String uploadImage(UUID id, MultipartFile file) {
        try {
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Solo se permiten imágenes");
            }

            if(file.getSize()/1024.0> 500){
                throw new RuntimeException("La imagen debería de pesar menos de 200 kb");
            }

            String extension = getExtension(file.getOriginalFilename());
            String key = "images/" + id + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return key;

        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a R2", e);
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    public String getPresignedUrl(String objectKey) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey) // Aquí va 'images/nombre.webp'
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15)) // El link expira en 15 mins
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }
}