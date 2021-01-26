package com.avenga.fil.lt.service;

import com.avenga.fil.lt.data.TextExtractInput;
import com.avenga.fil.lt.data.extract.Pages;

public interface TextExtractService {

    Pages extractText(TextExtractInput inputData);
}
