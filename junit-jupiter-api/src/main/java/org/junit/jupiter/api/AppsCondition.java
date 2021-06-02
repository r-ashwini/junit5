/*
 * Copyright 2015-2021 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api;

import java.io.Closeable;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppsCondition {
	private static final String VAR_PREFIX = "${";
	private static final String VAR_POSTFIX = "}";
	private static final long UNDEFINED = Long.MIN_VALUE;

	private AppsCondition() {
		throw new UnsupportedOperationException();
	}

	//Function to get system properties
	public static String getSystemProperty(final String key) {
		final Map<String, String> properties = getSystemProperties();
		return properties.get(key);
	}

	public static Map<String, String> getSystemProperties() {
		final Map<String, String> result = new HashMap<>();
		result.putAll(System.getenv());
		result.putAll(convertPropertiesToMap(System.getProperties()));
		return result;
	}

	public static Map<String, String> convertPropertiesToMap(final Properties properties) {
		final Map<String, String> result = new HashMap<>();
		for (final String name : properties.stringPropertyNames()) {
			result.put(name, properties.getProperty(name));
		}
		return result;
	}

	public static String injectProperties(final String text) {
		if (text != null && text.contains(VAR_PREFIX)) {
			String result = text;
			final Map<String, String> systemProperties = getSystemProperties();
			for (final Map.Entry<String, String> entry : systemProperties.entrySet()) {
				final String key = entry.getKey();
				final String value = entry.getValue();
				result = result.replace(VAR_PREFIX + key + VAR_POSTFIX, value);
			}
			return result;
		}
		return text;
	}

	public static boolean containsIgnoreCase(final String a, final String b) {
		return a != null && b != null && a.toLowerCase().contains(b.toLowerCase());
	}

	public static boolean hasAnyWithProperties(final String value, final String... variants) {
		for (final String operationSystem : variants) {
			final String injected = injectProperties(operationSystem);
			if (containsIgnoreCase(value, injected)) {
				return true;
			}
		}
		return false;
	}

	//pre requisite function for Is Socket opened
	public static boolean closeQuietly(final Closeable closeable) {
		try {
			closeable.close();
			return true;
		}
		catch (final Exception ex) {
			return false;
		}
	}

	//Function to check if socket is opened
	public static boolean isSocketOpened(final String host, final int port, final int timeout) throws Exception {
		Socket socket = null;
		try {
			socket = new Socket();
			socket.bind(null);
			socket.connect(new InetSocketAddress(host, port), timeout);
			return true;
		}
		catch (Exception e) {
			return false;
		}
		finally {
			closeQuietly(socket);
		}
	}

	//Function to check if machine has free space
	public static boolean isSatisfied(final String target, final long max, final long min) {
		final File file = new File(target);
		if (file.exists()) {
			final long freeSpace = file.getFreeSpace();
			if (min != UNDEFINED && freeSpace < min) {
				return false;
			}
			return max == UNDEFINED || freeSpace <= max;
		}
		return true;
	}

}
