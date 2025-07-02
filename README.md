package com.bartr.service.impl;

import com.bartr.service.PresignedUrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@Service
public class PresignedUrlServiceImpl implements PresignedUrlService {

    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;

    public String generatePresignedUrl(String fileName, String contentType) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();

        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest preq = presigner.presignPutObject(b ->
                b.signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(por)
        );
        presigner.close();
        return preq.url().toString();
    }
}
