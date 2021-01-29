package com.avenga.fil.lt.service.impl;

import com.avenga.fil.lt.data.extract.LineContent;
import com.avenga.fil.lt.data.extract.Pages;
import com.avenga.fil.lt.exception.PdfFormationException;
import com.avenga.fil.lt.service.DocumentFormationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.avenga.fil.lt.constants.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentFormationServiceImpl implements DocumentFormationService {

    private static final int X_AXIS_SHIFT = 595;
    private static final int Y_AXIS_SHIFT = 742;
    private static final float Y_AXIS_ABSOLUTE = 842f;
    private static final int FONT_SIZE = 8;
    private final Map<String, Function<String, byte[]>> typeResolver = Map.of(
        JPG, this::pdfFormation,
        JPEG, this::pdfFormation,
        PNG, this::pdfFormation,
        BMP, this::pdfFormation,
        XLS, this::xlsFormation,
        XLSX, this::xlsxFormation,
        PDF, this::pdfFormation
    );

    private final ObjectMapper objectMapper;

    @Override
    public byte[] formation(String documentType, String content) {
        return typeResolver.get(documentType.toLowerCase()).apply(content);
    }

    public byte[] pdfFormation(String content) {
        try{
            var pages = objectMapper.readValue(content, Pages.class).getContent();
            var document = new Document(PageSize.A4);
            var outputStream = new ByteArrayOutputStream();
            var writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            IntStream.range(0, pages.size()).forEach(pIndex -> addNextDocumentPage(pIndex, document, pages, writer));
            document.close();
            log.info(PDF_DOCUMENT_HAS_CREATED);
            return outputStream.toByteArray();
        }catch (Exception e) {
            throw new PdfFormationException(String.format(PDF_FORMATION_ERROR_MESSAGE, e.getMessage()));
        }
    }

    public byte[] xlsFormation(String content) {
        var document = excelFormation(new HSSFWorkbook(), content);
        log.info(XLS_DOCUMENT_HAS_CREATED);
        return document;
    }

    public byte[] xlsxFormation(String content) {
        var document = excelFormation(new XSSFWorkbook(), content);
        log.info(XLSX_DOCUMENT_HAS_CREATED);
        return document;
    }

    private byte[] excelFormation(Workbook workbook, String content) {
        try (var outputStream = new ByteArrayOutputStream()) {
            var sheetMatrix = objectMapper.readValue(content, new TypeReference<List<List<String>>>() {});
            fillRows(sheetMatrix, workbook.createSheet(TRANSLATED_SHEET));
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void fillRows(List<List<String>> content, Sheet sheet) {
        IntStream.range(0, content.size())
                .mapToObj(sheet::createRow)
                .forEach(row -> fillCells(row, content.get(row.getRowNum())));
    }

    private void fillCells(Row row, List<String> cells) {
        IntStream.range(0, cells.size())
                .forEach(cellIndex -> row.createCell(cellIndex).setCellValue(cells.get(cellIndex)));
    }

    private void addNextDocumentPage(int pageIndex, Document document, List<List<LineContent>> pages, PdfWriter writer) {
        pages.get(pageIndex).forEach(lc -> axisFormation(lc, writer));
        if (pageIndex < pages.size() - 1) {
            document.newPage();
        }
    }

    private void axisFormation(LineContent lineContent, PdfWriter writer) {
        try {
            var font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            addText(new String(lineContent.getText().getBytes(), StandardCharsets.UTF_8),
                    lineContent.getXAxis()*X_AXIS_SHIFT,
                    Y_AXIS_ABSOLUTE - (lineContent.getYAxis()*Y_AXIS_SHIFT), writer, font);
        } catch (Exception e) {
            throw new PdfFormationException(String.format(PDF_FORMATION_ERROR_MESSAGE, e.getMessage()));
        }
    }

    private void addText(String text, float x, float y, PdfWriter writer, BaseFont font) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            cb.beginText();
            cb.moveText(x, y);
            cb.setFontAndSize(font, DocumentFormationServiceImpl.FONT_SIZE);
            cb.showText(text);
            cb.endText();
            cb.restoreState();
    }
}
