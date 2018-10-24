package com.biit.rest.logger;

import com.biit.logger.BiitLogger;
import org.apache.log4j.Logger;

public class RestClientLogger extends BiitLogger {
	private static Logger logger = Logger.getLogger(RestClientLogger.class);

	/**
	 * Events that have business meaning (i.e. creating category, deleting form,
	 * ...). To follow user actions.
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param message
	 *            the text
	 */
	public static void info(String className, String message) {
		info(logger, className, message);
	}

	/**
	 * Shows not critical errors. I.e. Email address not found, permissions not
	 * allowed for this user, ...
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param message
	 *            the text
	 */
	public static void warning(String className, String message) {
		warning(logger, className, message);
	}

	/**
	 * For following the trace of the execution. I.e. Knowing if the application
	 * access to a method, opening database connection, etc.
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param message
	 *            the text
	 */
	public static void debug(String className, String message) {
		debug(logger, className, message);
	}

	/**
	 * To log any not expected error that can cause application malfunction.
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param message
	 *            the text
	 */
	public static void severe(String className, String message) {
		severe(logger, className, message);
	}

	/**
	 * To log java exceptions and log also the stack trace. If enabled, also can
	 * send an email to the administrator to alert of the error.
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param throwable
	 *            the exception
	 */
	public static void errorMessage(String className, Throwable throwable) {
		errorMessageNotification(logger, className, getStackTrace(throwable));
	}

	public static void errorMessage(Class<?> clazz, Throwable throwable) {
		errorMessageNotification(logger, clazz.getName(), getStackTrace(throwable));
	}

	/**
	 * To log java exceptions and log also the stack trace. If enabled, also can
	 * send an email to the administrator to alert of the error.
	 *
	 * @param className
	 *            the name of the class to log.
	 * @param error
	 *            the error message.
	 */
	public static void errorMessage(String className, String error) {
		errorMessageNotification(logger, className, error);
	}

	public static void errorMessage(Object object, Throwable throwable) {
		errorMessageNotification(logger, object.getClass().getName(), getStackTrace(throwable));
	}

	public static void sendNotification(String  className, String message) {
		sendNotification(logger, className, message);
	}
}
