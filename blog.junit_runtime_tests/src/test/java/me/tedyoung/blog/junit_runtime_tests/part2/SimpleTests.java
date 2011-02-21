package me.tedyoung.blog.junit_runtime_tests.part2;

import java.util.ArrayList;
import java.util.Collection;


import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FactoryRunner.class)
public class SimpleTests {
	@TestFactory
	public static Collection<?> tests() {
		ArrayList<SimpleRuntimeTestCase> tests = new ArrayList<SimpleRuntimeTestCase>();
		tests.add(new SimpleRuntimeTestCase("One"));
		tests.add(new SimpleRuntimeTestCase("Two"));
		return tests;
	}

	@Test
	public void test() {
		System.out.println("Hello World");
	}
}
