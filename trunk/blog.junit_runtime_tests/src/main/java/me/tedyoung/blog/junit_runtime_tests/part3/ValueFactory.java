package me.tedyoung.blog.junit_runtime_tests.part3;

import java.lang.reflect.Array;
import java.math.BigDecimal;

import org.easymock.EasyMock;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;

/**
 * Generates values of a given type.  Particularly useful in automated tests.
 */
public class ValueFactory {
	/**
	 * Creates a new ValueFactory.
	 */
	public ValueFactory() {
	}

	/**
	 * Generates a new value.  A best effort will be made
	 * to return a value different from currentValue.
	 * @param <T> the type of value.
	 * @param type the type of value.
	 * @param currentValue the current value.
	 * @return a new value.
	 */
	@SuppressWarnings("unchecked")
	public <T> T generateNewValue(Class<T> type, T currentValue) {
		if (type == Void.TYPE)
			return null;
		else if (type.isArray())
			return (T) generateNewArray(type);
		else if (ClassUtils.isPrimitiveWrapper(type) || type.isPrimitive())
			return (T) generateNewPrimitiveValue(type, currentValue);
		else if (type.isEnum())
			return generateNewEnumValue(type, currentValue);
		else
			return generateNewInstance(type, currentValue);
	}

	/**
	 * Generates a zero-length array of the given type.
	 * @param type the type of the array.
	 * @return a new array.
	 */
	private Object generateNewArray(Class<?> type) {
		return Array.newInstance(type.getComponentType(), 0);
	}

	/**
	 * Returns a new primitive value not equal to the current value.
	 * @param type the type of value.
	 * @param currentValue the current value not to duplicate.
	 * @return a new value.
	 */
	@SuppressWarnings("unchecked")
	private Object generateNewPrimitiveValue(Class<?> type, Object currentValue) {
		if (type == Boolean.class || type == Boolean.TYPE)
			return currentValue == null || currentValue.equals(Boolean.TRUE) ? Boolean.FALSE : Boolean.TRUE;
		else if (type == Character.class || type == Character.TYPE)
			return currentValue == null || currentValue.equals((char) 0) || currentValue.equals('B') ? 'A' : 'B';
		else
			return NumberUtils.convertNumberToTargetClass(currentValue == null || currentValue.equals(0) || currentValue.equals(0.0) ? 1 : 0, (Class<? extends Number>) ClassUtils.resolvePrimitiveIfNecessary(type));
	}

	/**
	 * Returns a different enum constant if possible.  Otherwise, returns the current value.
	 * @param <T> the enum type.
	 * @param type the enum type.
	 * @param currentValue the current value not to duplicate.
	 * @return a new enum constant.
	 */
	private <T> T generateNewEnumValue(Class<T> type, T currentValue) {
		for (T constant: type.getEnumConstants()) {
			if (constant != currentValue)
				return constant;
		}

		return currentValue;
	}

	/**
	 * Creates a new instance of a given class.  {@link BigDecimal} and {@link String} values
	 * will be not equal to the current value.  {@link Class} types will always return the current value.
	 * For any other type, an attempt will be made to instantiate a zero-arg constructor.  If
	 * the class cannot be instantiated, a mock will be returned instead.
	 * @param <T> the type of value.
	 * @param type the type of value.
	 * @param currentValue the current value not to duplicate.
	 * @return a new instance.
	 */
	@SuppressWarnings("unchecked")
	private <T> T generateNewInstance(Class<T> type, T currentValue) {
		if (type == BigDecimal.class) {
			return (T) (currentValue == null || ((BigDecimal) currentValue).equals(BigDecimal.ZERO) ? BigDecimal.ONE : BigDecimal.ZERO);
		}
		else if (type == String.class) {
			return (T) (currentValue == null || currentValue.equals("") ? "STRING" : "");
		}
		else if (type == Class.class) {
			return currentValue;
		}

		try {
			return BeanUtils.instantiateClass(type);
		}
		catch (BeanInstantiationException e) {
			return EasyMock.createMock(type);
		}
	}
}

