package me.tedyoung.blog.junit_runtime_tests.part4;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import me.tedyoung.blog.junit_runtime_tests.part2.FactoryRunner;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class DynamicRunner extends FactoryRunner {
	public DynamicRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void computeTests() throws InitializationError {
		try {
			tests.addAll(computeDataTests());
			super.computeTests();
		}
		catch (InitializationError e) {
			throw e;
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			throw new InitializationError(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<FrameworkMethod> computeDataTests() throws Exception {
		// A cache of all the test data mapped name to data.
		HashMap<String, Object> data = new HashMap<String, Object>();

		// Find all methods in our test class marked with @TestData.
		for (FrameworkMethod method: getTestClass().getAnnotatedMethods(TestData.class)) {
			// Make sure the TestData method is static
			if (!Modifier.isStatic(method.getMethod().getModifiers()))
				throw new InitializationError("TestData " + method + " must be static.");

			// Execute (statically) the TestData method and store its results in the data map.
			data.put(method.getAnnotation(TestData.class).value(),
				method.getMethod().invoke(getTestClass().getJavaClass()));
		}

		List<FrameworkMethod> tests = new LinkedList<FrameworkMethod>();

		// Find all methods in our test class marked with @DataTest.
		for (FrameworkMethod method: getTestClass().getAnnotatedMethods(DataTest.class)) {
			// Look up the test data by name.
			Object args = data.get(method.getAnnotation(DataTest.class).value());

			// Is the test data an array?  If so, make it a list.
			if (args.getClass().isArray())
				args = Arrays.asList((Object[]) args);

			// Is the test data a scalar object?  If so, put it in a list.
			if (!(args instanceof Iterable))
				args = Collections.singletonList(args);

			// For each record in the test data, we create a new test.
			for (Object arg: (Iterable<Object>) args) {
				// If the record was an array, we pass each array element as a seperate parameter.
				// Otherwise, we pass the record as a single parameter.
				if (arg.getClass().isArray())
					tests.add(new FrameworkDataTest(method.getMethod(), (Object[]) arg));
				else
					tests.add(new FrameworkDataTest(method.getMethod(), arg));
			}
		}

		return tests;
	}
}

