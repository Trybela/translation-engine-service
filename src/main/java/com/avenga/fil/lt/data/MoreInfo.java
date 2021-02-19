package com.avenga.fil.lt.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoreInfo {

    private String code;
    private List<String> developerMessage;
    private String userMessage;
    private String moreInfoMessage;
}
