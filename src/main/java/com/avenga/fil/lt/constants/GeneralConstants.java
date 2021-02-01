package com.avenga.fil.lt.constants;

public final class GeneralConstants {

    //LOGGING MESSAGE
    public static final String INCOMING_REQUEST_LOG_MESSAGE = "Starting to process the incoming request";
    public static final String FILE_IS_PRESENT_ON_S3 = "The file is present on s3";
    public static final String TEXT_EXTRACT_ENDED = "The text extraction process has ended.";
    public static final String PDF_DOCUMENT_HAS_CREATED = "Pdf document has successfully created";
    public static final String XLS_DOCUMENT_HAS_CREATED = "Xls document has successfully created";
    public static final String XLSX_DOCUMENT_HAS_CREATED = "Xlsx document has successfully created";

    //ERROR MESSAGE
    public static final String UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE = "Given %s content-type is not supported";
    public static final String UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE = "Given %s file type is not supported";
    public static final String ABSENT_REQUEST_HEADER_ERROR_MESSAGE = "Request headers is absent.";
    public static final String ABSENT_REQUEST_QUERY_PARAM_ERROR_MESSAGE = "Not all mandatory request parameters exist";
    public static final String ABSENT_FILE_ON_S3_BUCKET_ERROR_MESSAGE = "File %s is absent on S3 bucket!";
    public static final String TEXT_EXTRACT_PROCESS_ERROR_MESSAGE = "Error during extracting text process.";
    public static final String PDF_FORMATION_ERROR_MESSAGE = "Error during pdf document formation - %s";
    public static final String EXCEL_FORMATION_ERROR_MESSAGE = "Error during excel document formation -%s";

    //Response
    public static final String ERROR_RESPONSE_FORMAT = "{ \"errorMessage\": \"%s\" }";
    public static final String SUCCESS_RESPONSE = "Translation lambda completed successfully!";

    //General
    public static final String EXTENSION_DELIMITER = ".";
    public static final String NOT_EXIST_DEFAULT_VALUE = "Not existing default value";
    public static final String TRANSLATED_SHEET = "Translated sheet";
    public static final String TRANSLATED = "translated";

    private GeneralConstants() {
        throw new UnsupportedOperationException();
    }
}
