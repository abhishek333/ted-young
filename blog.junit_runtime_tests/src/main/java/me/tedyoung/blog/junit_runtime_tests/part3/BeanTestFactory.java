package me.tedyoung.blog.junit_runtime_tests.part3;


public class BeanTestFactory extends AbstractTestFactory {
	private Class<?> target;

	public BeanTestFactory(Class<?> target) {
		this.target = target;
	}

	public BeanTestFactory testProperty(String property, Object value) {
		add(new PropertyTester(newTargetInstance(), property, value));
		return this;
	}
	
	public BeanTestFactory testAllProperties() {
		add(PropertyTester.testAll(newTargetInstance()));
		return this;
	}
	
	public BeanTestFactory testConstructor(Object... arguments) {
		add(new ConstructorTester(target, arguments));
		return this;
	}


	private Object newTargetInstance() {
		try {
			return target.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

