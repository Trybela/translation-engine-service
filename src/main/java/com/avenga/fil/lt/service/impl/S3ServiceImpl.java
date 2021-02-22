package com.avenga.fil.lt.service.impl;

import com.avenga.fil.lt.data.FileStorageData;
import com.avenga.fil.lt.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    private final String bucketName;
    private final S3Client s3Client;

    public S3ServiceImpl(@Value("${s3.bucketName}") String bucketName, S3Client s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public FileStorageData getFile(String fileName) {
        return new FileStorageData(bucketName, fileName);
    }

    @Override
    public boolean isFileExists(String fileName) {
        try {
            var objectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            var file = s3Client.headObject(objectRequest);
            return file != null;
        } catch (SdkException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }
}
