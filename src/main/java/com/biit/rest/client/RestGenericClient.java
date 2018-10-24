package com.biit.rest.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.biit.logger.BiitCommonLogger;
import com.biit.rest.configuration.RestConfigurationReader;
import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.rest.logger.RestClientLogger;

/**
 * Generic rest client using Jersey API that returns a string.
 */
public class RestGenericClient {

	public static String post(boolean ssl, String target, String path, String message, String requestType, String messageType, boolean authentication,
			Map<String, Object> parameters) throws UnprocessableEntityException, EmptyResultException {

		HttpAuthenticationFeature authenticationFeature = null;
		if (authentication) {
			authenticationFeature = HttpAuthenticationFeature.basic(RestConfigurationReader.getInstance().getRestServiceUser(), RestConfigurationReader
					.getInstance().getRestServicePassword());
		}

		String response = null;
		RestClientLogger.debug(RestGenericClient.class.getName(), "Calling rest service (post) '" + target + "/" + path + "' with message:\n '" + message
				+ "'.");
		try {
			ClientBuilder builder = ClientBuilder.newBuilder();

			// Https
			if (ssl) {
				SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
				builder = builder.sslContext(sslContext);
			}

			// Enable authentication
			if (authentication && authenticationFeature != null) {
				builder = builder.register(authenticationFeature);
			}

			// Add Parameters
			WebTarget webTarget = builder.build().target(UriBuilder.fromUri(target).build()).path(path);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String, Object> record : parameters.entrySet()) {
					webTarget = webTarget.queryParam(record.getKey(), record.getValue());
				}
			}

			// Call the webservice
			response = webTarget.request(requestType).post(Entity.entity(message, messageType), String.class);

			RestClientLogger.debug(RestGenericClient.class.getName(), "Service returns '" + response + "'.");
			return response;
		} catch (Exception e) {
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
			RestClientLogger.severe(RestGenericClient.class.getName(), "Calling rest service '" + target + "/" + path + "' with message:\n '" + message
					+ "' error!");
			RestClientLogger.errorMessage(RestGenericClient.class.getName(), e);
		}
		return "";
	}

	public static String get(boolean ssl, String target, String path, String messageType, boolean authentication, Map<String, Object> parameters)
			throws UnprocessableEntityException, EmptyResultException {
		HttpAuthenticationFeature authenticationFeature = null;
		if (authentication) {
			authenticationFeature = HttpAuthenticationFeature.basic(RestConfigurationReader.getInstance().getRestServiceUser(), RestConfigurationReader
					.getInstance().getRestServicePassword());
		}

		String response = null;
		RestClientLogger.debug(RestGenericClient.class.getName(), "Calling rest service (get) '" + target + "/" + path + "'.");
		try {
			ClientBuilder builder = ClientBuilder.newBuilder();

			// Https
			if (ssl) {
				SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
				builder = builder.sslContext(sslContext);
			}

			// Enable authentication
			if (authentication && authenticationFeature != null) {
				builder = builder.register(authenticationFeature);
			}

			// Add Parameters
			WebTarget webTarget = builder.build().target(UriBuilder.fromUri(target).build()).path(path);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String, Object> record : parameters.entrySet()) {
					webTarget = webTarget.queryParam(record.getKey(), record.getValue());
				}
			}

			// Call the webservice
			response = webTarget.request().accept(messageType).get(String.class);

			RestClientLogger.debug(RestGenericClient.class.getName(), "Service returns '" + response + "'.");
			return response;
		} catch (Exception e) {
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
			RestClientLogger.severe(RestGenericClient.class.getName(), "Calling rest service '" + target + "/" + path + "'!");
			RestClientLogger.errorMessage(RestGenericClient.class.getName(), e);
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

		HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.basic(RestConfigurationReader.getInstance().getRestServiceUser(),
				RestConfigurationReader.getInstance().getRestServicePassword());
		Response response = null;
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
				byte[] bytes = toByteArray(result);
				return bytes;
			} catch (IOException e) {
				BiitCommonLogger.errorMessageNotification(RestGenericClient.class, e);
			}
		}
		return null;
	}

	private static byte[] toByteArray(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			baos.write(bytes, 0, read);
		}
		return baos.toByteArray();
	}
}
