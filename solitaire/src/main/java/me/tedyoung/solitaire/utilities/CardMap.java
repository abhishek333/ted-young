package me.tedyoung.solitaire.utilities;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;

import com.google.common.collect.AbstractIterator;

public class CardMap<V> extends AbstractMap<Card, V> {
	private List<V> entries = new ArrayList<>(52);
	private int size;

	public CardMap() {
		entries.addAll(Collections.nCopies(52, (V) null));
	}

	public CardMap(Map<Card, ? extends V> map) {
		putAll(map);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof Card)
			return entries.get(((Card) key).index()) != null;
		else
			return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return entries.contains(value);
	}

	@Override
	public V get(Object key) {
		return entries.get(((Card) key).index());
	}

	@Override
	public V put(Card key, V value) {
		V oldValue = get(key);
		entries.set(key.index(), value);
		if (oldValue == null && value != null)
			size++;
		else if (oldValue != null && value == null)
			size--;
		return oldValue;
	}

	@Override
	public V remove(Object key) {
		V value = get(key);
		if (containsKey(key)) {
			put((Card) key, null);
			size--;
		}
		return value;
	}

	@Override
	public void putAll(Map<? extends Card, ? extends V> map) {
		for (Map.Entry<? extends Card, ? extends V> entry : map.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear() {
		size = 0;
		Collections.fill(entries, null);
	}

	@Override
	public Set<Card> keySet() {
		return new AbstractSet<Card>() {
			@Override
			public Iterator<Card> iterator() {
				return new NullSkippingIterator<Card>() {
					@Override
					protected Card value(Card card, V value) {
						return card;
					}
				};
			}

			@Override
			public int size() {
				return CardMap.this.size();
			}

			@Override
			public void clear() {
				CardMap.this.clear();
			}

			@Override
			public boolean isEmpty() {
				return CardMap.this.isEmpty();
			}

			@Override
			public boolean remove(Object o) {
				return CardMap.this.remove(o) != null;
			}
		};
	}

	@Override
	public Collection<V> values() {
		return new AbstractSet<V>() {
			@Override
			public Iterator<V> iterator() {
				return new NullSkippingIterator<V>() {
					@Override
					protected V value(Card card, V value) {
						return value;
					}
				};
			}

			@Override
			public int size() {
				return CardMap.this.size();
			}

			@Override
			public void clear() {
				CardMap.this.clear();
			}

			@Override
			public boolean isEmpty() {
				return CardMap.this.isEmpty();
			}

			@Override
			public boolean remove(Object o) {
				int index = entries.indexOf(o);
				if (index == -1)
					return false;
				entries.set(index, null);
				return true;
			}
		};
	}

	@Override
	public Set<Map.Entry<Card, V>> entrySet() {
		return new AbstractSet<Map.Entry<Card, V>>() {
			@Override
			public Iterator<Map.Entry<Card, V>> iterator() {
				return new NullSkippingIterator<Map.Entry<Card, V>>() {
					@Override
					protected Map.Entry<Card, V> value(Card card, V value) {
						return new Entry(card);
					}
				};
			}

			@Override
			public int size() {
				return CardMap.this.size();
			}

			@Override
			public void clear() {
				CardMap.this.clear();
			}

			@Override
			public boolean isEmpty() {
				return CardMap.this.isEmpty();
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean remove(Object o) {
				return CardMap.this.remove(((Map.Entry<Card, V>) o).getKey()) != null;
			}
		};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		result = prime * result + size;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardMap other = (CardMap) obj;
		if (size != other.size)
			return false;
		if (entries == null) {
			if (other.entries != null)
				return false;
		}
		else if (!entries.equals(other.entries))
			return false;
		return true;
	}

	private class Entry implements Map.Entry<Card, V> {
		private final Card card;

		public Entry(Card card) {
			this.card = card;
		}

		@Override
		public Card getKey() {
			return card;
		}

		@Override
		public V getValue() {
			return CardMap.this.get(card);
		}

		@Override
		public V setValue(V value) {
			return CardMap.this.put(card, value);
		}
	}

	private abstract class NullSkippingIterator<T> extends AbstractIterator<T> {
		ListIterator<V> iterator = entries.listIterator();

		@Override
		protected T computeNext() {
			V value = null;
			while (iterator.hasNext() && value == null)
				value = iterator.next();

			if (value == null)
				return endOfData();

			return value(Card.get(iterator.previousIndex()), value);
		}

		protected abstract T value(Card card, V value);
	}

}
