package me.tedyoung.blog.junit_runtime_tests.mocks;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MockingRunner extends BlockJUnit4ClassRunner {
	public MockingRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
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
