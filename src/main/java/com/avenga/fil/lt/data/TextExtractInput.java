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
    private String fileType;
}
