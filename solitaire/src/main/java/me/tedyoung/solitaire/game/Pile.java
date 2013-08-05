package me.tedyoung.solitaire.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class Pile implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int LAST = Integer.MAX_VALUE;

	private ArrayList<Card> cards;
	private int firstVisibleCardIndex;
	private int minimumVisible;

	private transient boolean copyOnWrite;
	private transient Integer hashCode;

	public Pile() {
		cards = new ArrayList<>(26);
	}

	private Pile(Pile pile) {
		this.cards = pile.cards;
		this.firstVisibleCardIndex = pile.firstVisibleCardIndex;
		this.minimumVisible = pile.minimumVisible;
		pile.copyOnWrite = true;
	}

	private void prepareForUpdate() {
		if (copyOnWrite) {
			ArrayList<Card> cards = this.cards;
			this.cards = new ArrayList<>(26);
			this.cards.addAll(cards);
			copyOnWrite = false;
		}
		this.hashCode = null;
	}

	public void add(int index, boolean visible, List<Card> cards) {
		if (visible) {
			index += firstVisibleCardIndex;
			if (index > getAll().size())
				index = getAll().size();
		}
		else {
			if (index > firstVisibleCardIndex)
				index = firstVisibleCardIndex;
			firstVisibleCardIndex += cards.size();
		}

		prepareForUpdate();

		this.cards.addAll(index, cards);

		updateVisible();
	}

	public void add(int index, boolean visible, Card card) {
		add(index, visible, Collections.singletonList(card));
	}

	public void hideAll() {
		firstVisibleCardIndex = getAll().size();
		updateVisible();
		this.hashCode = null;
	}

	public void show(int count) {
		firstVisibleCardIndex -= count;
		if (firstVisibleCardIndex < 0)
			firstVisibleCardIndex = 0;
		this.hashCode = null;
	}

	public Card remove(Card card) {
		return remove(getVisible().indexOf(card));
	}

	public Card remove(int index) {
		if (getVisible().isEmpty() || index == -1)
			return null;

		prepareForUpdate();
		Card card = getVisible().remove(index);
		updateVisible();
		return card;
	}

	public List<Card> remove(int start, int end) {
		prepareForUpdate();
		List<Card> list = getVisible().subList(start, end);
		ArrayList<Card> cards = new ArrayList<Card>(list);
		list.clear();
		updateVisible();
		return cards;
	}

	public List<Card> deal(int count) {
		if (count > firstVisibleCardIndex)
			count = firstVisibleCardIndex;

		prepareForUpdate();
		ArrayList<Card> cards = new ArrayList<Card>(this.cards.subList(0, count));
		this.cards.subList(0, count).clear();
		firstVisibleCardIndex -= count;

		return cards;
	}

	public List<List<Card>> group(int size) {
		return Lists.partition(getAll(), size);
	}

	public void redact() {
		prepareForUpdate();
		Collections.fill(getHidden(), Card.UNKNOWN);
	}

	public List<Card> getVisible() {
		return cards.subList(firstVisibleCardIndex, getAll().size());
	}

	public List<Card> getHidden() {
		return Lists.reverse(cards.subList(0, firstVisibleCardIndex));
	}

	public List<Card> getAll() {
		return Lists.reverse(cards);
	}

	public void setMinimumVisible(int minimumVisible) {
		this.minimumVisible = minimumVisible;
	}

	private void updateVisible() {
		if (getVisible().size() < minimumVisible)
			show(minimumVisible - getVisible().size());
	}

	int getFirstVisibleCardIndex() {
		return firstVisibleCardIndex;
	}

	void setFirstVisibleCardIndex(int firstVisibleCardIndex) {
		this.firstVisibleCardIndex = firstVisibleCardIndex;
	}

	@Override
	public Pile clone() {
		return new Pile(this);
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cards == null) ? 0 : cards.hashCode());
			result = prime * result + firstVisibleCardIndex;
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pile other = (Pile) obj;

		if (this.hashCode() != other.hashCode())
			return false;

		if (cards == null) {
			if (other.cards != null)
				return false;
		}
		else if (!cards.equals(other.cards))
			return false;
		if (firstVisibleCardIndex != other.firstVisibleCardIndex)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[firstVisibleCardIndex=" + firstVisibleCardIndex + ", cards=" + cards + "]";
	}

}
