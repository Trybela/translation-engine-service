package com.avenga.fil.lt.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextTranslateInput {

    private String sourceLanguage;
    private String targetLanguage;
    private String text;
    private String fileType;
}
