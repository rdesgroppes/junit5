/*
 * Copyright 2015-2023 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.engine.discovery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.platform.AbstractEqualsAndHashCodeTests;
import org.junit.platform.commons.PreconditionViolationException;

/**
 * Unit tests for {@link ClassSelector}.
 *
 * @since 1.3
 * @see DiscoverySelectorsTests
 */
class ClassSelectorTests extends AbstractEqualsAndHashCodeTests {

	@Test
	void equalsAndHashCode() {
		var selector1 = new ClassSelector("org.example.TestClass", null);
		var selector2 = new ClassSelector("org.example.TestClass", null);
		var selector3 = new ClassSelector("org.example.X", null);

		assertEqualsAndHashCode(selector1, selector2, selector3);
	}

	@Test
	void preservesOriginalExceptionWhenTryingToLoadClass() {
		var selector = new ClassSelector("org.example.TestClass", null);

		var e = assertThrows(PreconditionViolationException.class, selector::getJavaClass);

		assertThat(e).hasMessage("Could not load class with name: org.example.TestClass").hasCauseInstanceOf(
			ClassNotFoundException.class);
	}

	@Test
	void usesClassClassLoader() {
		var selector = new ClassSelector(getClass());

		assertThat(selector.getClassLoader()).isNotNull().isSameAs(getClass().getClassLoader());
	}

}
