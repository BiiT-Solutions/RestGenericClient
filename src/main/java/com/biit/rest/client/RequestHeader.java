package com.biit.rest.client;

/**
 * Different headers that are present on a response.
 */
public enum RequestHeader {

    AUTHORIZATION("Authorization");

    private final String tag;

    RequestHeader(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
