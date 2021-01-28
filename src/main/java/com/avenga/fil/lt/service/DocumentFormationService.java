package com.avenga.fil.lt.service;

import com.avenga.fil.lt.data.extract.LineContent;

import java.util.List;

public interface DocumentFormationService {

    byte[] pdfFormation(List<List<LineContent>> pages);
}
