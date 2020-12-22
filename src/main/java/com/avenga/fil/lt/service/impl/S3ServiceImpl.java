package com.avenga.fil.lt.service.impl;

import com.avenga.fil.lt.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service {

    private final String bucketName;
    private final S3Client s3Client;

    public S3ServiceImpl(@Value("${s3.bucketName}") String bucketName, S3Client s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public void saveFile(String fileName, byte[] binaryFile, String contentType) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(binaryFile));
    }
}
