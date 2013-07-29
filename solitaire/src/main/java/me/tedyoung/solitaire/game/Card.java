package me.tedyoung.solitaire.game;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card implements Comparable<Card>, Serializable {
	private static final long serialVersionUID = 1L;

	public static final Card UNKNOWN = new Card(null, null);

	private static final ArrayList<Card> cards = new ArrayList<>(52);

	static {
		for (Suit suit : Suit.values())
			for (Denomination denomination : Denomination.values())
				cards.add(new Card(denomination, suit));

	}

	public static Card get(Suit suit, Denomination denomination) {
		return cards.get(suit.ordinal() * 13 + denomination.ordinal());
	}

	public static Card get(int index) {
		return cards.get(index);
	}

	public static List<Card> getAll() {
		return new ArrayList<>(cards);
	}

	public static List<Card> getAll(Suit suit) {
		return new ArrayList<>(cards.subList(suit.ordinal() * 13, suit.ordinal() * 13 + 13));
	}

	public static List<Card> getAll(Denomination denomination) {
		ArrayList<Card> list = new ArrayList<>(4);
		for (Suit suit : Suit.values())
			list.add(get(suit, denomination));
		return list;
	}

	public static List<Card> getAll(Suit suit, Denomination from, Denomination to) {
		return new ArrayList<>(cards.subList(suit.ordinal() * 13 + from.ordinal(), suit.ordinal() * 13 + to.ordinal() + 1));
	}

	public static List<Card> getAll(Denomination from, Denomination to) {
		ArrayList<Card> list = new ArrayList<>(52);
		for (Suit suit : Suit.values())
			list.addAll(getAll(suit, from, to));
		return list;
	}

	private final Denomination denomination;
	private final Suit suit;

	private Card(Denomination denomination, Suit suit) {
		this.denomination = denomination;
		this.suit = suit;
	}

	public Denomination getDenomination() {
		return denomination;
	}

	public Suit getSuit() {
		return suit;
	}

	public int index() {
		return suit.ordinal() * 13 + denomination.ordinal();
	}

	public Card getPeer() {
		if (this == UNKNOWN)
			return null;

		return get(suit.getOtherSuitThisColor(), denomination);
	}

	public boolean isPeerOf(Card that) {
		if (this == UNKNOWN || that == UNKNOWN)
			return false;

		return this.denomination == that.denomination && this.suit.getOtherSuitThisColor() == that.suit;
	}

	public List<Card> getHolders() {
		if (this == UNKNOWN || denomination == Denomination.KING)
			return Collections.emptyList();

		ArrayList<Card> list = new ArrayList<>(2);
		for (Suit suit : this.suit.getSuitsNotThisColor())
			list.add(get(suit, denomination.next()));
		return list;
	}

	public boolean isHolderOf(Card that) {
		if (this == UNKNOWN || that == UNKNOWN || that.denomination == Denomination.KING)
			return false;

		return !this.suit.isSameColor(that.suit) && this.denomination.isOneGreaterThan(that.denomination);
	}

	public List<Card> getPredecessors() {
		if (this == UNKNOWN || denomination == Denomination.ACE)
			return Collections.emptyList();

		return Card.getAll(suit, Denomination.ACE, denomination.previous());
	}

	public Card getPredecessor() {
		if (this == UNKNOWN || denomination == Denomination.ACE)
			return null;

		return Card.get(suit, denomination.previous());
	}

	public boolean isPredecessorOf(Card that) {
		if (this == UNKNOWN || that == UNKNOWN)
			return false;

		return that.suit == this.suit && this.denomination.compareTo(that.denomination) < 0;
	}

	public Card getSuccessor() {
		if (this == UNKNOWN || denomination == Denomination.ACE)
			return null;

		return Card.get(suit, denomination.next());
	}

	private Object readResolve() throws ObjectStreamException {
		return get(suit, denomination);
	}

	@Override
	public int compareTo(Card that) {
		if (this == that)
			return 0;
		else if (this == UNKNOWN)
			return -1;
		else if (that == UNKNOWN)
			return 1;

		if (this.denomination != that.denomination)
			return this.denomination.compareTo(that.denomination);
		else
			return this.suit.compareTo(that.suit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((denomination == null) ? 0 : denomination.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (denomination != other.denomination)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return (denomination == null ? "?" : denomination).toString() + (suit == null ? "?" : suit).toString();
	}

}
