package com.avenga.fil.lt.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("application/pdf"),
    JPG("image/jpeg"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    BMP("image/bmp"),
    XLS("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    TXT("text/plain");

    private final String contentType;

}
