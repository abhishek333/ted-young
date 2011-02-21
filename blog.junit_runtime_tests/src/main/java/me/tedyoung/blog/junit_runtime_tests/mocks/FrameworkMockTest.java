package me.tedyoung.blog.junit_runtime_tests.mocks;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runners.model.FrameworkMethod;

class FrameworkMockTest extends FrameworkMethod {
	public FrameworkMockTest(Method method) {
		super(method);
	}

	@Override
	public Object invokeExplosively(final Object target, Object... oldParams) throws Throwable {
		final Method method = getMethod();
		Class<?>[] paramTypes = method.getParameterTypes();
		final Object[] params = new Object[paramTypes.length];

		// For each test method parameter, create a mock.
		for (int i = 0; i < paramTypes.length; i++)
			params[i] = EasyMock.createStrictMock(paramTypes[i]);

		return new ReflectiveCallable() {
			@Override
			protected Object runReflectiveCall() throws Throwable {
				// Call the test method with the mocks.
				Object o = method.invoke(target, params);

				// Verify them afterwards.
				for (Object param: params)
					EasyMock.verify(param);

				return o;
			}
		}.run();
	}
}