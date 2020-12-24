package com.avenga.fil.lt.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestPayloadData {

    private String fromLanguage;
    private String toLanguage;
    private String userId;
    private String fileName;
    private String fileType;
    private String contentType;
    private String body;
}
