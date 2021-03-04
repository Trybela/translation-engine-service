package com.avenga.fil.lt.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Builder
public class Link {

    private final String rel = "translate-status";
    private String href;
    private final HttpMethod method = HttpMethod.GET;

}
