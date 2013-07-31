package me.tedyoung.solitaire.game;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Card implements Comparable<Card>, Serializable {
	SA(Denomination.ACE, Suit.SPADES), S2(Denomination.TWO, Suit.SPADES), S3(Denomination.THREE, Suit.SPADES), S4(Denomination.FOUR, Suit.SPADES), S5(Denomination.FIVE, Suit.SPADES), S6(Denomination.SIX, Suit.SPADES), S7(Denomination.SEVEN, Suit.SPADES), S8(Denomination.EIGHT, Suit.SPADES), S9(Denomination.NINE, Suit.SPADES), ST(Denomination.TEN, Suit.SPADES), SJ(Denomination.JACK, Suit.SPADES), SQ(Denomination.QUEEN, Suit.SPADES), SK(Denomination.KING, Suit.SPADES), HA(Denomination.ACE,
			Suit.HEARTS), H2(Denomination.TWO, Suit.HEARTS), H3(Denomination.THREE, Suit.HEARTS), H4(Denomination.FOUR, Suit.HEARTS), H5(Denomination.FIVE, Suit.HEARTS), H6(Denomination.SIX, Suit.HEARTS), H7(Denomination.SEVEN, Suit.HEARTS), H8(Denomination.EIGHT, Suit.HEARTS), H9(Denomination.NINE, Suit.HEARTS), HT(Denomination.TEN, Suit.HEARTS), HJ(Denomination.JACK, Suit.HEARTS), HQ(Denomination.QUEEN, Suit.HEARTS), HK(Denomination.KING, Suit.HEARTS), CA(Denomination.ACE, Suit.CLUBS), C2(
			Denomination.TWO, Suit.CLUBS), C3(Denomination.THREE, Suit.CLUBS), C4(Denomination.FOUR, Suit.CLUBS), C5(Denomination.FIVE, Suit.CLUBS), C6(Denomination.SIX, Suit.CLUBS), C7(Denomination.SEVEN, Suit.CLUBS), C8(Denomination.EIGHT, Suit.CLUBS), C9(Denomination.NINE, Suit.CLUBS), CT(Denomination.TEN, Suit.CLUBS), CJ(Denomination.JACK, Suit.CLUBS), CQ(Denomination.QUEEN, Suit.CLUBS), CK(Denomination.KING, Suit.CLUBS), DA(Denomination.ACE, Suit.DIAMONDS), D2(Denomination.TWO,
			Suit.DIAMONDS), D3(Denomination.THREE, Suit.DIAMONDS), D4(Denomination.FOUR, Suit.DIAMONDS), D5(Denomination.FIVE, Suit.DIAMONDS), D6(Denomination.SIX, Suit.DIAMONDS), D7(Denomination.SEVEN, Suit.DIAMONDS), D8(Denomination.EIGHT, Suit.DIAMONDS), D9(Denomination.NINE, Suit.DIAMONDS), DT(Denomination.TEN, Suit.DIAMONDS), DJ(Denomination.JACK, Suit.DIAMONDS), DQ(Denomination.QUEEN, Suit.DIAMONDS), DK(Denomination.KING, Suit.DIAMONDS), UNKNOWN(null, null);

	private static final List<Card> cards = unmodifiableList(Arrays.asList(values()).subList(0, 52));

	public static Card get(int index) {
		return values()[index];
	}

	public static Card get(Suit suit, Denomination denomination) {
		return get(suit.ordinal() * 13 + denomination.ordinal());
	}

	public static List<Card> getAll() {
		return cards;
	}

	public static List<Card> getAll(Suit suit) {
		return cards.subList(suit.ordinal() * 13, suit.ordinal() * 13 + 13);
	}

	public static List<Card> getAll(Denomination denomination) {
		ArrayList<Card> list = new ArrayList<>(4);
		for (Suit suit : Suit.values())
			list.add(get(suit, denomination));
		return unmodifiableList(list);
	}

	public static List<Card> getAll(Suit suit, Denomination from, Denomination to) {
		return cards.subList(suit.ordinal() * 13 + from.ordinal(), suit.ordinal() * 13 + to.ordinal() + 1);
	}

	public static List<Card> getAll(Denomination from, Denomination to) {
		ArrayList<Card> list = new ArrayList<>(52);
		for (Suit suit : Suit.values())
			list.addAll(getAll(suit, from, to));
		return unmodifiableList(list);
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

	@Override
	public String toString() {
		return (denomination == null ? "?" : denomination).toString() + (suit == null ? "?" : suit).toString();
	}
}
