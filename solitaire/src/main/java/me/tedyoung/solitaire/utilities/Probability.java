package me.tedyoung.solitaire.utilities;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.AbstractIterator;
import com.google.common.math.BigIntegerMath;

public class Probability {

	public static <T extends Comparable<T>> Set<Set<T>> combinations(Collection<T> contents, int choose) {
		return new CombinationCollection<>(contents, choose);
	}

	private static class CombinationCollection<T extends Comparable<T>> extends AbstractSet<Set<T>> {
		private final int choose;
		private final ArrayList<T> elements;

		public CombinationCollection(Collection<T> contents, int choose) {
			this.choose = choose;
			elements = new ArrayList<>(contents);
			Collections.sort(elements);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Set))
				return false;
			Set<T> set = (Set<T>) o;
			return set.size() == choose && elements.containsAll(set);
		}

		@Override
		public Iterator<Set<T>> iterator() {
			return new CombinationCollectionIterator<T>(elements, choose);
		}

		@Override
		public int size() {
			return BigIntegerMath.binomial(elements.size(), choose).intValue();
		}

	}

	private static class CombinationCollectionIterator<T extends Comparable<T>> extends AbstractIterator<Set<T>> {

		private final List<T> elements;
		private final int[] indexes;

		public CombinationCollectionIterator(List<T> elements, int choose) {
			this.elements = elements;
			this.indexes = new int[choose];
			for (int index = 0; index < indexes.length; index++)
				indexes[index] = indexes.length - index - 1;
			indexes[0]--;
		}

		@Override
		protected Set<T> computeNext() {
			int index = 0;
			while (index < indexes.length && indexes[index] == elements.size() - index - 1)
				index++;

			if (index == indexes.length)
				return endOfData();

			indexes[index]++;

			while (--index >= 0)
				indexes[index] = indexes[index + 1] + 1;

			Set<T> set = new HashSet<>();
			for (int i : indexes)
				set.add(elements.get(i));

			return set;

		}
	}
}
