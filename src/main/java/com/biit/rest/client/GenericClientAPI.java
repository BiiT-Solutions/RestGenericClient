package com.biit.rest.client;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.rest.logger.RestClientLogger;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class GenericClientAPI {
    public static final String DEFAULT_HEALTH_CHECK_PATH = "/health-check";

    private String baseUrl;
    private String path;
    private int portNumber;

    public GenericClientAPI() {
        this.path = "";
    }

    public GenericClientAPI(String baseUrl, int portNumber) {
        this();
        this.baseUrl = baseUrl;
        this.portNumber = portNumber;
    }

    public boolean healthCheck() {
        try {
            Response result = RestGenericClient.get(isSSL(), getBaseUrlWithPort(), getPath()
                    + getHealthCheckPath(), MediaType.MEDIA_TYPE_WILDCARD, false, null);
            return result != null && result.getStatus() >= HttpsURLConnection.HTTP_OK && result.getStatus() < HttpsURLConnection.HTTP_MULT_CHOICE;
        } catch (UnprocessableEntityException | EmptyResultException e) {
            RestClientLogger.errorMessage(this.getClass(), e);
            return false;
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    protected boolean isSSL() {
        return getBaseUrl().startsWith("https");
    }

    public String getBaseUrlWithPort() {
        return getBaseUrl() + ':' + portNumber;
    }

    protected String getHealthCheckPath() {
        return DEFAULT_HEALTH_CHECK_PATH;
    }
}
