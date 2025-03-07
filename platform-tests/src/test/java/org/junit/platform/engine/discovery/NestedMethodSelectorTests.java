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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.AbstractEqualsAndHashCodeTests;
import org.junit.platform.commons.PreconditionViolationException;

/**
 * Unit tests for {@link NestedMethodSelector}.
 *
 * @since 1.6
 * @see DiscoverySelectorsTests
 */
class NestedMethodSelectorTests extends AbstractEqualsAndHashCodeTests {

	@Test
	void equalsAndHashCode() {
		var selector1 = new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "int, boolean",
			null);
		var selector2 = new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "int, boolean",
			null);

		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("X"), "NestedTestClass", "method", "int, boolean", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("X"), "NestedTestClass", "method", "", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "int", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "X", "int, boolean", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "X", "", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "X", "method", "int, boolean", null));
		assertEqualsAndHashCode(selector1, selector2,
			new NestedMethodSelector(List.of("EnclosingClass"), "X", "method", "", null));
	}

	@Test
	void preservesOriginalExceptionWhenTryingToLoadEnclosingClass() {
		var selector = new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "int, boolean",
			null);

		var exception = assertThrows(PreconditionViolationException.class, selector::getEnclosingClasses);

		assertThat(exception).hasMessage("Could not load class with name: EnclosingClass") //
				.hasCauseInstanceOf(ClassNotFoundException.class);
	}

	@Test
	void preservesOriginalExceptionWhenTryingToLoadNestedClass() {
		var selector = new NestedMethodSelector(List.of("EnclosingClass"), "NestedTestClass", "method", "int, boolean",
			null);

		var exception = assertThrows(PreconditionViolationException.class, selector::getNestedClass);

		assertThat(exception).hasMessage("Could not load class with name: NestedTestClass") //
				.hasCauseInstanceOf(ClassNotFoundException.class);
	}

	@Test
	void usesClassClassLoader() {
		var selector = new NestedMethodSelector(List.of(getClass()), NestedTestCase.class, "method", "");

		assertThat(selector.getClassLoader()).isNotNull().isSameAs(getClass().getClassLoader());
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	class NestedTestCase {
		void method() {
		}
	}

}
