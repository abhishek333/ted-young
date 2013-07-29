package me.tedyoung.solitaire.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		this.copyOnWrite = pile.copyOnWrite = true;
	}

	private void prepareForUpdate() {
		if (copyOnWrite) {
			ArrayList<Card> cards = this.cards;
			this.cards = new ArrayList<>(26);
			this.cards.addAll(cards);
		}
		this.hashCode = null;
	}

	public void add(int index, boolean visible, List<Card> cards) {
		if (visible) {
			index += firstVisibleCardIndex;
			if (index > getSize())
				index = getSize();
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
		firstVisibleCardIndex = getSize();
		updateVisible();
		this.hashCode = null;
	}

	public void show(int count) {
		firstVisibleCardIndex -= count;
		if (firstVisibleCardIndex < 0)
			firstVisibleCardIndex = 0;
		this.hashCode = null;
	}

	public Card getTop() {
		return get(0);
	}

	public Card getBottom() {
		return get(getVisibleSize() - 1);
	}

	public Card get(int index) {
		if (!isAnyVisible())
			return null;
		else
			return getVisible().get(index);
	}

	public Card removeTop() {
		return remove(0);
	}

	public Card remove(Card card) {
		return remove(indexOf(card));
	}

	public Card remove(int index) {
		if (!isAnyVisible() || index == -1)
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
		ArrayList<List<Card>> groups = new ArrayList<>();
		for (int index = cards.size(); index > 0; index -= size)
			groups.add(cards.subList(Math.max(index - size, 0), index));
		return groups;
	}

	public void redact() {
		prepareForUpdate();
		Collections.fill(getHidden(), Card.UNKNOWN);
	}

	public List<Card> getVisible() {
		return cards.subList(firstVisibleCardIndex, getSize());
	}

	public List<Card> getHidden() {
		return cards.subList(0, firstVisibleCardIndex);
	}

	public List<Card> getAll() {
		return cards;
	}

	public boolean isAnyVisible() {
		return getVisibleSize() != 0;
	}

	public boolean isAnyHidden() {
		return getHiddenSize() != 0;
	}

	public int indexOf(Card card) {
		return getVisible().lastIndexOf(card);
	}

	public boolean contains(Card card) {
		return cards.contains(card);
	}

	public int getSize() {
		return cards.size();
	}

	public int getVisibleSize() {
		return isEmpty() ? 0 : getSize() - firstVisibleCardIndex;
	}

	public int getHiddenSize() {
		return isEmpty() ? 0 : firstVisibleCardIndex;
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void setMinimumVisible(int minimumVisible) {
		this.minimumVisible = minimumVisible;
	}

	private void updateVisible() {
		if (getVisibleSize() < minimumVisible)
			show(minimumVisible - getVisibleSize());
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

	int getFirstVisibleCardIndex() {
		return firstVisibleCardIndex;
	}

	void setFirstVisibleCardIndex(int firstVisibleCardIndex) {
		this.firstVisibleCardIndex = firstVisibleCardIndex;
	}

	ArrayList<Card> getCards() {
		return cards;
	}
}
