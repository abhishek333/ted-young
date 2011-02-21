package me.tedyoung.blog.junit_runtime_tests.part3;

import java.util.Collection;
import java.util.LinkedList;

public abstract class AbstractTestFactory implements TestFactory {
	private LinkedList<Object> tests = new LinkedList<Object>();

	@Override
	public Collection<Object> getTests() {
		return this.tests;
	}

	@Override
	public void addTests(Collection<Object> tests) {
		this.tests.addAll(tests);
	}

	protected void add(Object test) {
		this.tests.add(test);
	}

	protected void add(Collection<Object> tests) {
		this.tests.addAll(tests);
	}
	
	@Override
	public <T extends TestFactory> T and(T factory) {
		factory.addTests(getTests());
		return factory;
	}
}
