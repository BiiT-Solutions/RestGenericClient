package com.biit.rest.configuration;

import java.nio.file.Path;

import com.biit.rest.logger.RestClientLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;
import com.biit.utils.file.watcher.FileWatcher.FileModifiedListener;

public class RestConfigurationReader extends ConfigurationReader {
	private static final String CONFIG_FILE = "settings.conf";
	private static final String SYSTEM_VARIABLE_CONFIG = "REST_CONFIG";

	private static RestConfigurationReader instance;

	private static final String ID_REST_SERVICE_USER = "web.service.rest.user";
	private static final String ID_REST_SERVICE_PASS = "web.service.rest.password";

	private RestConfigurationReader() {
		super();
		addProperty(ID_REST_SERVICE_USER, "");
		addProperty(ID_REST_SERVICE_PASS, "");

		PropertiesSourceFile sourceFile = new PropertiesSourceFile(CONFIG_FILE);
		sourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				RestClientLogger.info(this.getClass().getName(), "WAR settings file '" + pathToFile + "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(sourceFile);
		SystemVariablePropertiesSourceFile systemSourceFile = new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE_CONFIG, CONFIG_FILE);
		systemSourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				RestClientLogger.info(this.getClass().getName(), "System variable settings file '" + pathToFile + "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(systemSourceFile);

		readConfigurations();
	}

	private static void createInstance() {
		if (instance == null) {
			synchronized (RestConfigurationReader.class) {
				if (instance == null) {
					instance = new RestConfigurationReader();
				}
			}
		}
	}

	public static RestConfigurationReader getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			RestClientLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public String getRestServiceUser() {
		return getPropertyLogException(ID_REST_SERVICE_USER);
	}

	public String getRestServicePassword() {
		return getPropertyLogException(ID_REST_SERVICE_PASS);
	}

}
