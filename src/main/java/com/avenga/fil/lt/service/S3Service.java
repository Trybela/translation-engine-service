package com.avenga.fil.lt.service;

import com.avenga.fil.lt.data.FileStorageData;

public interface S3Service {

    FileStorageData saveFile(String fileName, String fileType, String userId, byte[] binaryFile, String contentType);

    FileStorageData getFile(String fileName);

    boolean isFileExists(String fileName);
}
