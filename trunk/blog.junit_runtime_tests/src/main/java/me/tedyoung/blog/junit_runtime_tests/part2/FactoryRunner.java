package me.tedyoung.blog.junit_runtime_tests.part2;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

public class FactoryRunner extends BlockJUnit4ClassRunner {
	protected LinkedList<FrameworkMethod> tests = new LinkedList<FrameworkMethod>();

	public FactoryRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		try {
			computeTests();
		}
		catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	protected void computeTests() throws Exception {
		tests.addAll(super.computeTestMethods());
		tests.addAll(computeFactoryTests());
		
		// This is called here to ensure the test class constructor is called at least
		// once during testing.  If a test class has only TestFactories, than the 
		// test class will never be instantiated by JUnit.
		createTest();
	}
	
	protected Collection<? extends FrameworkMethod> computeFactoryTests() throws Exception {
		List<FrameworkFactoryTest> tests = new LinkedList<FrameworkFactoryTest>();

		// Final all methods in our test class marked with @TestFactory.
		for (FrameworkMethod method: getTestClass().getAnnotatedMethods(TestFactory.class)) {
			// Make sure the TestFactory method is static
			if (!Modifier.isStatic(method.getMethod().getModifiers()))
				throw new InitializationError("TestFactory " + method + " must be static.");

			// Execute the method (statically)
			Object instances = method.getMethod().invoke(getTestClass().getJavaClass());

			// Did the factory return an array?  If so, make it a list.
			if (instances.getClass().isArray())
				instances = Arrays.asList((Object[]) instances);

			// Did the factory return a scalar object?  If so, put it in a list.
			if (!(instances instanceof Iterable<?>))
				instances = Collections.singletonList(instances);

			// For each object returned by the factory.
			for (Object instance: (Iterable<?>) instances) {
				// Find any methods marked with @FactoryTest.
				for (FrameworkMethod m: new TestClass(instance.getClass()).getAnnotatedMethods(FactoryTest.class))
					tests.add(new FrameworkFactoryTest(m.getMethod(), instance, method.getName()));
			}
		}

		return tests;
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.junit.runners.BlockJUnit4ClassRunner#computeTestMethods()
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		return tests;
	}

	@Override
	protected void validateInstanceMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(After.class, false, errors);
		validatePublicVoidNoArgMethods(Before.class, false, errors);
		validateTestMethods(errors);
	}
}



