package me.tedyoung.blog.junit_runtime_tests.part4;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;
import org.springframework.util.StringUtils;

class FrameworkDataTest extends FrameworkMethod {
	private Object[] args;

	public FrameworkDataTest(Method method, Object... args) {
		super(method);
		this.args = args;
	}

	@Override
	public Object invokeExplosively(Object target, Object... params) throws Throwable {
		return super.invokeExplosively(target, args);
	}

	@Override
	public String getName() {
		return String.format("%s[%s]", super.getName(),
			StringUtils.arrayToCommaDelimitedString(args));
	}
}