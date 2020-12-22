package com.avenga.fil.lt.constants;

public final class GeneralConstants {

    //LOGGING MESSAGE
    public static final String INCOMING_REQUEST_LOG_MESSAGE = "Starting to process the incoming request";
    public static final String FILE_SUCCESSFULLY_UPLOADED = "Incoming file has uploaded to s3";

    //ERROR MESSAGE
    public static final String UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE = "Given %s content-type is not supported";
    public static final String UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE = "Given %s file type is not supported";

    private GeneralConstants() {
        throw new UnsupportedOperationException();
    }
}
