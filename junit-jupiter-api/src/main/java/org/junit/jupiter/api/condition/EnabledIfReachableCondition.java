/*
 * Copyright 2015-2021 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.condition;

import static java.lang.String.format;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/*CUSTOM CONDITION - To enable test if the URL is reachable */
public class EnabledIfReachableCondition implements ExecutionCondition {
	private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = ConditionEvaluationResult.enabled(
		"@EnabledIfReachable is not present");
	// Finding the annotation in execution context and returning the evaluation result
	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		AnnotatedElement element = context.getElement().orElseThrow(IllegalStateException::new);
		return findAnnotation(element, EnabledIfReachable.class).map(
			annotation -> disableIfUnreachable(annotation, element)).orElse(ENABLED_BY_DEFAULT);
	}

	// Evaluating the condition
	private ConditionEvaluationResult disableIfUnreachable(EnabledIfReachable annotation, AnnotatedElement element) {
		String url = annotation.url();
		int timeoutMillis = annotation.timeoutMillis();
		boolean reachable = checkURL_response(url, timeoutMillis);
		if (reachable)
			return enabled(format("%s is enabled because %s is reachable", element, url));
		else
			return disabled(
				format("%s is disabled because %s could not be reached in %dms", element, url, timeoutMillis));
	}

	// helper method to check response code of code of web URL
	public boolean checkURL_response(String url, int timeout) {
		url = url.replaceFirst("^https", "http");
		// Otherwise an exception may be thrown on invalid SSL certificates.
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setConnectTimeout(timeout);
			con.setReadTimeout(timeout);
			con.setRequestMethod("HEAD");
			int responseCode = con.getResponseCode();
			return (200 <= responseCode && responseCode <= 399);
		}
		catch (IOException exception) {
			return false;
		}
	}
}
