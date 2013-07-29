package me.tedyoung.solitaire.utilities;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtilities {
	private CollectionUtilities() {
	}

	public static String join(String delimeter, Collection<?> collection) {
		StringBuilder builder = new StringBuilder();
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next());
			if (iterator.hasNext())
				builder.append(delimeter);
		}
		return builder.toString();
	}
}
