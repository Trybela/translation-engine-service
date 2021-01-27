package com.avenga.fil.lt.service.impl;

import com.avenga.fil.lt.data.FileStorageData;
import com.avenga.fil.lt.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static com.avenga.fil.lt.constants.GeneralConstants.EXTENSION_DELIMITER;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    private static final String DATE_TIME_PATTERN = "yyyyMMddHHmmss";
    private static final String FILE_NAME_DELIMITER = "_";

    private final String bucketName;
    private final S3Client s3Client;

    public S3ServiceImpl(@Value("${s3.bucketName}") String bucketName, S3Client s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public FileStorageData saveFile(String fileName, String fileType, String userId, byte[] binaryFile, String contentType) {
        var fileKey = constructFileName(fileName, fileType, userId);
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(contentType)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(binaryFile));
        return new FileStorageData(bucketName, fileKey);
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

    private String constructFileName(String fileName, String fileType, String userId) {
        var dateTime = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now());
        return new StringJoiner(FILE_NAME_DELIMITER).add(fileName).add(userId).add(dateTime).toString() +
                EXTENSION_DELIMITER + fileType;
    }
}
