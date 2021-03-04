package com.avenga.fil.lt.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextExtractInput {

    private String bucketName;
    private String fileKey;
    private String fromLanguage;
    private String toLanguage;
    private String userId;
    private String documentName;
    private String fileType;
    private String unit;
    private String xlsColumns;

    public TextExtractInput(String documentName, FileStorageData fileStorageData, RequestPayloadData requestData) {
        this.bucketName = fileStorageData.getBucketName();
        this.fileKey = fileStorageData.getFileKey();
        this.fromLanguage = requestData.getFromLanguage();
        this.toLanguage = requestData.getToLanguage();
        this.userId = requestData.getUserId();
        this.documentName = documentName;
        this.fileType = requestData.getFileType();
        this.unit = requestData.getUnit();
        this.xlsColumns = requestData.isApplyXlsRules() ? requestData.getXlsColumns() : "";
    }
}
