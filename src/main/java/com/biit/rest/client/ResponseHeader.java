package com.biit.rest.client;

/**
 * Different headers that are present on a response.
 */
public enum ResponseHeader {

    AUTHORIZATION("Authorization"),

    CONTENT_TYPE("Content-Type"),

    DATE("Date"),

    EXPIRES("Expires");

    private final String tag;

    ResponseHeader(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
