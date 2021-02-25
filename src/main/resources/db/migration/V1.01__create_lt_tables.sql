CREATE TABLE lt_extract_data
(
    lt_extract_data_key SERIAL PRIMARY KEY,
    user_id             VARCHAR(255) NOT NULL,
    unit                VARCHAR(255) NULL,
    document_name       VARCHAR(255) NOT NULL,
    page_count          INT4         NOT NULL DEFAULT 1,
    extract_date_time   TIMESTAMP    NOT NULL
);

CREATE TABLE lt_translate_data
(
    lt_translate_data_key SERIAL PRIMARY KEY,
    user_id               VARCHAR(255) NOT NULL,
    unit                  VARCHAR(255) NULL,
    document_name         VARCHAR(255) NOT NULL,
    text_length           INT4         NOT NULL,
    translate_date_time   TIMESTAMP    NOT NULL
);
