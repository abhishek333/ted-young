package me.tedyoung.blog.junit_runtime_tests.part3;

public class PhoneNumber {
	private String value;

	public PhoneNumber(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
