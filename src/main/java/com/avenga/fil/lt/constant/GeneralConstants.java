package com.avenga.fil.lt.constant;

public final class GeneralConstants {

    //LOGGING MESSAGE
    public static final String INCOMING_REQUEST_LOG_MESSAGE = "Starting to process the incoming request";
    public static final String FILE_IS_PRESENT_ON_S3 = "The file is present on s3";
    public static final String TEXT_EXTRACT_LAMBDA_INVOKED = "The text extraction process has invoked.";

    //ERROR MESSAGE
    public static final String UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE = "Given %s file type is not supported";
    public static final String ABSENT_REQUEST_HEADER_ERROR_MESSAGE = "Request header %s is absent";
    public static final String EMPTY_REQUEST_HEADER_ERROR_MESSAGE = "Request header %s is empty";
    public static final String ABSENT_REQUEST_QUERY_PARAM_ERROR_MESSAGE = "Not all mandatory request parameters exist";
    public static final String ABSENT_FILE_ON_S3_BUCKET_ERROR_MESSAGE = "File %s is absent on S3 bucket!";
    public static final String TEXT_EXTRACT_PROCESS_ERROR_MESSAGE = "Error during invoke extracting text process";
    public static final String WRONG_INPUT_FORMAT_ERROR_MESSAGE = "xlsColumns %s has wrong format. Supported input format: 1,4,5 or 2-4 or mix of these two formats";

    //Response
    public static final String ERROR_RESPONSE_FORMAT = "ErrorMessage: %s";

    //General
    public static final String EXTENSION_DELIMITER = ".";
    public static final String TRANSLATED = "translated";

    private GeneralConstants() {
        throw new UnsupportedOperationException();
    }
}
