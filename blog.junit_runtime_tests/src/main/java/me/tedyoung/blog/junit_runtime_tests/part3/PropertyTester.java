package me.tedyoung.blog.junit_runtime_tests.part3;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.LinkedList;

import junit.framework.Assert;
import me.tedyoung.blog.junit_runtime_tests.part2.FactoryTest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class PropertyTester {
	private static ValueFactory valueFactory = new ValueFactory();
	
	// An object from the Spring API that provides convenient reflection-bases access
	// to the object we are testing.
	private BeanWrapper wrapper;

	// The property name to test.
	private String property;

	// The value to set the property to.
	private Object value;

	public PropertyTester(Object target, String property, Object value) {
		this.wrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
		this.property = property;
		this.value = value;
	}

	@FactoryTest
	public void test() {
		wrapper.setPropertyValue(property, value);
		Assert.assertEquals(value, wrapper.getPropertyValue(property));
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Object> testAll(Object target) {
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
		Collection<Object> tests = new LinkedList<Object>();

		// For each property.
		for (PropertyDescriptor descriptor: BeanUtils.getPropertyDescriptors(target.getClass())) {
			// Get the property name.
			String name = descriptor.getName();
			// Add the test if the property is read/write.
			if (descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
				tests.add(
					new PropertyTester(target, name,
						valueFactory.generateNewValue(wrapper.getPropertyType(name), wrapper.getPropertyValue(name))
					)
				);
			}
		}

		return tests;
	}
}

