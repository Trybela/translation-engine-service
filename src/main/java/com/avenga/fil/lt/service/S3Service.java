package com.avenga.fil.lt.service;

public interface S3Service {

    void saveFile(String fileName, byte[] binaryFile, String contentType);
}
