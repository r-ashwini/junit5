/*
 * Copyright 2015-2021 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.extension;

import static java.lang.System.currentTimeMillis;
import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

import org.junit.jupiter.api.Benchmark;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public class BenchmarkExtension implements Extension, BeforeAllCallback, BeforeTestExecutionCallback,
		AfterTestExecutionCallback, AfterAllCallback {
	private static final Namespace NAMESPACE = Namespace.create("org", "codefx", "BenchmarkExtension");
	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if (!shouldBeBenchmarked(context))
			return;
		long launchTime = loadLaunchTime(context, LaunchTimeKey.CLASS);
		long elapsedTime = currentTimeMillis() - launchTime;
		report("Test container", context, elapsedTime);
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		if (!shouldBeBenchmarked(context))
			return;
		long launchTime = loadLaunchTime(context, LaunchTimeKey.TEST);
		long elapsedTime = currentTimeMillis() - launchTime;
		report("Test", context, elapsedTime);
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if (!shouldBeBenchmarked(context))
			return;
		storeNowAsLaunchTime(context, LaunchTimeKey.CLASS);
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		if (!shouldBeBenchmarked(context))
			return;
		storeNowAsLaunchTime(context, LaunchTimeKey.TEST);
	}

	private static boolean shouldBeBenchmarked(ExtensionContext context) {
		return context.getElement().map(el -> isAnnotated(el, Benchmark.class)).orElse(false);
	}

	private static void storeNowAsLaunchTime(ExtensionContext context, LaunchTimeKey key) {
		context.getStore(NAMESPACE).put(key, currentTimeMillis());
	}

	private static long loadLaunchTime(ExtensionContext context, LaunchTimeKey key) {
		return context.getStore(NAMESPACE).get(key, long.class);
	}

	private static void report(String unit, ExtensionContext context, long elapsedTime) {
		String message = String.format(" %s '%s' took %d ms.", unit, context.getDisplayName(), elapsedTime);
		context.publishReportEntry("Benchmark", message);
	}

	private enum LaunchTimeKey {
		CLASS, TEST
	}
}
