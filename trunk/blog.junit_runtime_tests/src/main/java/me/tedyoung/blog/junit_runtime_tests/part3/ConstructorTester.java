package me.tedyoung.blog.junit_runtime_tests.part3;

import java.lang.reflect.Constructor;

import junit.framework.Assert;

import me.tedyoung.blog.junit_runtime_tests.part2.FactoryTest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

public class ConstructorTester {
	// Spring API: gets the names of parameters.
	private LocalVariableTableParameterNameDiscoverer namer =
			new LocalVariableTableParameterNameDiscoverer();

	// The class of object being tested.
	private Class<?> type;

	// The constructor arguments to use.
	private Object[] arguments;

	// The constructor argument types.
	private Class<?>[] types;
	
	public ConstructorTester(Class<?> type, Object[] arguments) {
		this.type = type;
		this.arguments = arguments;
		
		this.types = new Class<?>[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			types[i] = arguments[i].getClass();
	}

	@FactoryTest
	public void test() {
		// Locate the constructor.
		Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(type, types);
		// Get the parameter names of the constructor.
		String[] parameterNames = namer.getParameterNames(constructor);
		// Instantiate the target using the constructor and arguments.
		Object target = BeanUtils.instantiateClass(constructor, arguments);
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);

		// For each parameter, ensure the property of the same name is the same value.
		for (int i = 0; i < arguments.length; i++)
			Assert.assertEquals(parameterNames[i], arguments[i], wrapper.getPropertyValue(parameterNames[i]));
	}
}
