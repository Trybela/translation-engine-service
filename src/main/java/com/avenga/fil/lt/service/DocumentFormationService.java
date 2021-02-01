package com.avenga.fil.lt.service;

import com.avenga.fil.lt.data.FileType;

public interface DocumentFormationService {

    byte[] formation(FileType documentType, String content);
}
