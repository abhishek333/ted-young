package me.tedyoung.blog.junit_runtime_tests.mocks;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import me.tedyoung.blog.junit_runtime_tests.part4.DynamicRunner;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MockingDynamicRunner extends DynamicRunner {
	public MockingDynamicRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void computeTests() throws InitializationError {
		try {
			tests.addAll(computetMockTests());
			tests.addAll(computeDataTests());
			tests.addAll(computeFactoryTests());
		}
		catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	private Collection<FrameworkMethod> computetMockTests() throws Exception {
		List<FrameworkMethod> tests = new LinkedList<FrameworkMethod>();

		for (FrameworkMethod method: getTestClass().getAnnotatedMethods(Test.class))
			tests.add(new FrameworkMockTest(method.getMethod()));

		return tests;
	}
	
	// Disable this check; it is now valid to have arguments on test methods.
	@Override
	protected void validatePublicVoidNoArgMethods( Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
	}
}

