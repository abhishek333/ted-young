package me.tedyoung.blog.junit_runtime_tests.part2;


public class SimpleRuntimeTestCase {
	String message;

	public SimpleRuntimeTestCase(String message) {
		this.message = message;
	}

	@FactoryTest
	public void test() {
		System.out.println(message);
	}
}

