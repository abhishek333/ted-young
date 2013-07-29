package me.tedyoung.solitaire.utilities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class CachingComparator<T> implements Comparator<T> {
	private Map<T, Integer> cache = new HashMap<>();

	@Override
	public int compare(T one, T two) {
		return Integer.compare(cachedValueOf(one), cachedValueOf(two));
	}

	private int cachedValueOf(T object) {
		Integer value = cache.get(object);
		if (value == null) {
			value = valueOf(object);
			cache.put(object, value);
		}
		return value;
	}

	protected abstract int valueOf(T object);
}
