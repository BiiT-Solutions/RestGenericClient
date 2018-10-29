package com.biit.rest.client;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;

import javax.ws.rs.core.MediaType;

public abstract class GenericClientAPI {

	private String baseUrl;
	private String path;
	private int portNumber;
	public static final String DEFAULT_HEALTH_CHECK_PATH = "/healthCheck";

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
			String result = RestGenericClient.get(isSSL(), getBaseUrlWithPort(), getPath() + getHealthCheckPath(), MediaType.MEDIA_TYPE_WILDCARD, false, null);
			return result != null && !result.isEmpty();
		} catch (UnprocessableEntityException | EmptyResultException e) {
			e.printStackTrace();
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
