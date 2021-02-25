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
    private String documentName;
    private String fileType;
    private String unit;
}
