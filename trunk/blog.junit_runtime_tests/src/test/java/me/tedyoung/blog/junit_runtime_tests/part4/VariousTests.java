package me.tedyoung.blog.junit_runtime_tests.part4;

import junit.framework.Assert;

import org.junit.runner.RunWith;

@RunWith(DynamicRunner.class)
public class VariousTests {
	@TestData("integers")
	public static Object[] integerData() {
		return new Integer[]{1, 2, 3, 4, 5};
	}

	@TestData("strings")
	public static String[] stringData() {
		return new String[]{"abc", "def", "ghi"};
	}

	@DataTest("integers")
	public void squareTest(int i) {
		Assert.assertEquals(i * i, Utils.square(i));
	}

	@DataTest("integers")
	public void cubeTests(int i) {
		Assert.assertEquals(i * i * i, Utils.cube(i));
	}

	@DataTest("strings")
	public void reverseTests(String s) {
		Assert.assertEquals(s.hashCode(), Utils.hash(s));
	}
}
