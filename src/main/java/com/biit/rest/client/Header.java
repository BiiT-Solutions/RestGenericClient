package com.biit.rest.client;

public class Header {
    private String name;
    private String value;

    public Header() {
        super();
    }

    public Header(String name, String value) {
        this();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
