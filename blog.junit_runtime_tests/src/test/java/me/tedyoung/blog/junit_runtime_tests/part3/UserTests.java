package me.tedyoung.blog.junit_runtime_tests.part3;

import java.util.Collection;

import me.tedyoung.blog.junit_runtime_tests.part2.FactoryRunner;
import me.tedyoung.blog.junit_runtime_tests.part2.TestFactory;

import org.junit.runner.RunWith;

@RunWith(FactoryRunner.class)
public class UserTests {
	@TestFactory
	public static Collection<?> factory() {
		return new BeanTestFactory(User.class)
			.testAllProperties()
			.testConstructor("username", "password")
			.getTests();
	}
}
