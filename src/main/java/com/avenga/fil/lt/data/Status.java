package com.avenga.fil.lt.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Status {

    private String translatedDocumentName;
    private List<Link> links;
}
