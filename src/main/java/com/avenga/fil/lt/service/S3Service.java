package com.avenga.fil.lt.service;

public interface S3Service {

    void saveFile(String fileName, String fileType, String userId, byte[] binaryFile, String contentType);
}
