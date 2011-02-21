package me.tedyoung.blog.junit_runtime_tests.part4;

public class Utils {
	public static Object square(int i) {
		return i * i;
	}

	public static Object cube(int i) {
		return i * i * i;
	}

	public static Object hash(String s) {
		return s.hashCode();
	}
}
