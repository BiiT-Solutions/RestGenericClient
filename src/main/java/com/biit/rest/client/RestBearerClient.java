package com.biit.rest.client;

import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.logger.BiitCommonLogger;
import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.rest.logger.RestClientLogger;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.net.ssl.SSLContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Generic rest client using Jersey API that returns a string.
 */
public class RestBearerClient {

    public static String post(String target, String path, String message, String requestType, String messageType, String encodedToken,
                              Map<String, Object> parameters) throws UnprocessableEntityException, EmptyResultException {
        String response;
        RestClientLogger.debug(RestBearerClient.class.getName(),
                "Calling rest service (post) '" + target + (!target.endsWith("/") ? "/" : "") + path + "' with message:\n '" + message + "'.");
        try {
            ClientBuilder builder = ClientBuilder.newBuilder();

            // Https
            if (target.startsWith("https")) {
                SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
                builder = builder.sslContext(sslContext);
            }

            // Add Parameters
            WebTarget webTarget = builder.build().target(UriBuilder.fromUri(target).build()).path(path);
            if (parameters != null && !parameters.isEmpty()) {
                for (Entry<String, Object> record : parameters.entrySet()) {
                    webTarget = webTarget.queryParam(record.getKey(), record.getValue());
                }
            }

            // Call the webservice
            response = webTarget.request(requestType).header(HttpHeaders.AUTHORIZATION, "Bearer " + encodedToken)
                    .post(Entity.entity(message, messageType), String.class);

            RestClientLogger.debug(RestBearerClient.class.getName(), "Service returns '" + response + "'.");
            return response;
        } catch (Exception e) {
            RestClientLogger.severe(RestBearerClient.class.getName(),
                    "Error calling rest service (post) '" + target + (!target.endsWith("/") ? "/" : "") + path + "' with message:\n '" + message + "'.");
            if (e instanceof ClientErrorException) {
                if (e.getMessage().contains("HTTP 422")) {
                    throw new UnprocessableEntityException(e.getMessage(), e);
                } else if (e.getMessage().contains("HTTP 406")) {
                    throw new EmptyResultException(e.getMessage(), e);
                }
            }
            throw e;
        }
    }

    public static String get(String target, String path, String messageType, String encodedToken, Map<String, Object> parameters)
            throws UnprocessableEntityException, EmptyResultException {

        String response;
        RestClientLogger.debug(RestBearerClient.class.getName(), "Calling rest service (get) '" + target +
                (!target.endsWith("/") ? "/" : "") + path + "'.");
        try {
            ClientBuilder builder = ClientBuilder.newBuilder();

            // Https
            if (target.startsWith("https")) {
                SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
                builder = builder.sslContext(sslContext);
            }
            // Add Parameters
            WebTarget webTarget = builder.build().target(UriBuilder.fromUri(target).build()).path(path);
            if (parameters != null && !parameters.isEmpty()) {
                for (Entry<String, Object> record : parameters.entrySet()) {
                    webTarget = webTarget.queryParam(record.getKey(), record.getValue());
                }
            }

            // Call the webservice
            response = webTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + encodedToken).accept(messageType).get(String.class);

            RestClientLogger.debug(RestBearerClient.class.getName(), "Service returns '" + response + "'.");
            return response;
        } catch (ProcessingException e) {
            RestClientLogger.severe(RestBearerClient.class.getName(), "Invalid request to '" + target + (!target.endsWith("/") ? "/" : "") + path + "'.");
        } catch (Exception e) {
            RestClientLogger.severe(RestBearerClient.class.getName(), "Error calling rest rest service (get) '" + target + (!target.endsWith("/") ? "/" : "") + path + "'.");
            if (e instanceof ClientErrorException) {
                if (e.getMessage().contains("HTTP 422")) {
                    UnprocessableEntityException uee = new UnprocessableEntityException(e.getMessage());
                    uee.setStackTrace(e.getStackTrace());
                    throw uee;
                } else if (e.getMessage().contains("HTTP 406")) {
                    EmptyResultException uee = new EmptyResultException(e.getMessage());
                    uee.setStackTrace(e.getStackTrace());
                    throw uee;
                }
            }
            RestClientLogger.severe(RestBearerClient.class.getName(), "Calling rest service '" + target + (!target.endsWith("/") ? "/" : "") + path + "'!");
            RestClientLogger.errorMessage(RestBearerClient.class.getName(), e);
        }
        return "";
    }

    public static byte[] callRestServiceGetJpgImage(String targetPath, String path, String json) {
        return postForImage(targetPath, path, "image/jpg", json);
    }

    public static byte[] callRestServiceGetPngImage(String targetPath, String path, String json) {
        return postForImage(targetPath, path, "image/png", json);
    }

    private static byte[] postForImage(String target, String path, String requestType, String json) {
        boolean ssl = target.startsWith("https");

        HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.basic(LiferayConfigurationReader.getInstance().getUser(),
                LiferayConfigurationReader.getInstance().getPassword());
        Response response;
        if (ssl) {
            SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
            response = ClientBuilder.newBuilder().sslContext(sslContext).build().target(UriBuilder.fromUri(target).build()).path(path)
                    .register(authenticationFeature).request(requestType).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        } else {
            response = ClientBuilder.newBuilder().build().target(UriBuilder.fromUri(target).build()).path(path).register(authenticationFeature)
                    .request(requestType).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        }
        if (response.getStatusInfo().toString().equals(Response.Status.OK.toString())) {
            InputStream result = response.readEntity(InputStream.class);
            try {
                return toByteArray(result);
            } catch (IOException e) {
                BiitCommonLogger.errorMessageNotification(RestBearerClient.class, e);
            }
        }
        return null;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            baos.write(bytes, 0, read);
        }
        return baos.toByteArray();
    }
}
