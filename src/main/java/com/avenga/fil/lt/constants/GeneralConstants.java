package com.avenga.fil.lt.constants;

public final class GeneralConstants {

    //LOGGING MESSAGE
    public static final String INCOMING_REQUEST_LOG_MESSAGE = "Starting to process the incoming request";
    public static final String FILE_SUCCESSFULLY_UPLOADED = "Incoming file has uploaded to s3";
    public static final String NOT_EXIST_DEFAULT_VALUE = "Not existing default value";

    //ERROR MESSAGE
    public static final String UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE = "Given %s content-type is not supported";
    public static final String UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE = "Given %s file type is not supported";
    public static final String ABSENT_REQUEST_HEADER_ERROR_MESSAGE = "Request headers is absent.";
    public static final String ABSENT_REQUEST_QUERY_PARAM_ERROR_MESSAGE = "Not all mandatory request parameters exist";
    public static final String ABSENT_REQUEST_BODY_ERROR_MESSAGE = "Request body in empty!";

    //General
    public static final String EXTENSION_DELIMITER = ".";

    private GeneralConstants() {
        throw new UnsupportedOperationException();
    }
}
