package me.tedyoung.blog.junit_runtime_tests.part3;

import java.util.Collection;

public interface TestFactory {
	Collection<Object> getTests();
	
	void addTests(Collection<Object> tests);

	<T extends TestFactory> T and(T factory);
}