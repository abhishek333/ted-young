package me.tedyoung.solitaire.game;

import static me.tedyoung.solitaire.game.Denomination.ACE;
import static me.tedyoung.solitaire.game.Denomination.KING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import me.tedyoung.solitaire.game.errors.CardNotAvailableFromTopOfFoundationException;
import me.tedyoung.solitaire.game.errors.InvalidCardPlayedToFoundationException;

public class MutableFoundation implements Foundation, Serializable {
	private static final long serialVersionUID = 1L;

	private EnumMap<Suit, Denomination> denominations = new EnumMap<>(Suit.class);

	public MutableFoundation() {
		for (Suit suit : Suit.values())
			denominations.put(suit, null);
	}

	MutableFoundation(MutableFoundation foundation) {
		this.denominations = new EnumMap<>(foundation.denominations);
	}

	@Override
	public boolean isPlayable(Card card) {
		if (card.equals(Card.UNKNOWN))
			return false;

		Denomination denomination = denominations.get(card.getSuit());

		if (denomination == null)
			return card.getDenomination() == ACE;
		else
			return card.getDenomination().isOneGreaterThan(denomination);
	}

	public void addCard(Card card) {
		if (!isPlayable(card))
			throw new InvalidCardPlayedToFoundationException(this, card);

		denominations.put(card.getSuit(), card.getDenomination());
	}

	public Card removeCard(Card card) {
		if (!card.equals(getTopCard(card.getSuit())))
			throw new CardNotAvailableFromTopOfFoundationException(card);

		denominations.put(card.getSuit(), denominations.get(card.getSuit()).previous());
		return card;
	}

	@Override
	public List<Card> getTopCards() {
		ArrayList<Card> cards = new ArrayList<Card>(4);
		for (Suit suit : Suit.values())
			if (!isEmpty(suit))
				cards.add(getTopCard(suit));
		return cards;
	}

	@Override
	public Card getTopCard(Suit suit) {
		if (isEmpty(suit))
			return null;
		else
			return Card.get(suit, denominations.get(suit));
	}

	public boolean containsCard(Card card) {
		if (isEmpty(card.getSuit()))
			return false;
		return getTopCard(card.getSuit()).getDenomination().compareTo(card.getDenomination()) >= 0;
	}

	public List<Card> getCards() {
		ArrayList<Card> cards = new ArrayList<Card>(size());
		for (Suit suit : Suit.values())
			cards.addAll(getCards(suit));
		return cards;
	}

	public List<Card> getCards(Suit suit) {
		if (isEmpty(suit))
			return Collections.emptyList();

		return Card.getAll(suit, ACE, getCurrentDenomination(suit));
	}

	@Override
	public Denomination getCurrentDenomination(Suit suit) {
		return denominations.get(suit);
	}

	@Override
	public boolean isEmpty() {
		for (Suit suit : Suit.values())
			if (!isEmpty(suit))
				return false;
		return true;
	}

	@Override
	public boolean isEmpty(Suit suit) {
		return getCurrentDenomination(suit) == null;
	}

	@Override
	public boolean isComplete() {
		for (Suit suit : Suit.values())
			if (!isComplete(suit))
				return false;
		return true;
	}

	@Override
	public boolean isComplete(Suit suit) {
		return getCurrentDenomination(suit) == KING;
	}

	@Override
	public int size() {
		int size = 0;

		for (Suit suit : Suit.values())
			size += size(suit);

		return size;
	}

	@Override
	public int size(Suit suit) {
		if (isEmpty(suit))
			return 0;
		else
			return getCurrentDenomination(suit).ordinal() + 1;
	}

	@Override
	public MutableFoundation clone() {
		return new MutableFoundation(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((denominations == null) ? 0 : denominations.hashCode());
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
		MutableFoundation other = (MutableFoundation) obj;
		if (denominations == null) {
			if (other.denominations != null)
				return false;
		}
		else if (!denominations.equals(other.denominations))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MutableFoundation " + denominations;
	}

}
