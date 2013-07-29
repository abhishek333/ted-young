package me.tedyoung.solitaire.game;

import static me.tedyoung.solitaire.game.Denomination.KING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.tedyoung.solitaire.game.errors.CardNotAvailableFromTopOfPileException;
import me.tedyoung.solitaire.game.errors.CardNotFoundException;
import me.tedyoung.solitaire.game.errors.InvalidCardPlayedToStackException;

public class MutableStack implements Stack, Comparable<MutableStack>, Serializable {
	private static final long serialVersionUID = 1L;

	private Pile cards;

	// Note: not considered in equals or hash code or serialize
	private int index;

	public MutableStack(int index, List<Card> cards) {
		this.index = index;
		this.cards = new Pile();
		this.cards.setMinimumVisible(1);
		this.cards.add(0, false, cards);
	}

	MutableStack(MutableStack stack) {
		this.index = stack.index;
		this.cards = stack.cards.clone();
	}

	@Override
	public boolean isPlayable(Card card) {
		if (card.equals(Card.UNKNOWN))
			return false;

		if (isEmpty())
			return card.getDenomination() == KING;

		Card topCard = getTopCard();
		if (topCard.equals(Card.UNKNOWN))
			return false;

		return topCard.isHolderOf(card);
	}

	public void addCard(Card card) {
		addCards(Collections.singletonList(card));
	}

	public void addCards(List<Card> cards) {
		// Assumes cards are sorted.
		if (!isPlayable(cards.get(cards.size() - 1)))
			throw new InvalidCardPlayedToStackException(this, cards.get(cards.size() - 1));

		this.cards.add(0, true, cards);
	}

	@Override
	public List<Card> getVisibleCards() {
		return Collections.unmodifiableList(cards.getVisible());
	}

	public List<Card> getHiddenCards() {
		List<Card> list = new ArrayList<>(cards.getHidden());
		Collections.reverse(list);
		return Collections.unmodifiableList(list);
	}

	public List<Card> getAllCards() {
		List<Card> list = new ArrayList<>(getSize());
		list.addAll(getVisibleCards());
		list.addAll(getHiddenCards());
		return Collections.unmodifiableList(list);
	}

	public List<Card> removeCardsFrom(Card card) {
		int index = getIndexOfCard(card);
		if (index == -1)
			throw new CardNotFoundException(this, card);

		return cards.remove(0, index + 1);
	}

	public Card removeCard(Card card) {
		if (isEmpty() || !card.equals(getTopCard()))
			throw new CardNotAvailableFromTopOfPileException(this, card);

		return removeTopCard();
	};

	public Card removeTopCard() {
		return cards.removeTop();
	}

	@Override
	public Card getTopCard() {
		return cards.getTop();
	}

	@Override
	public Card getLastVisibleCard() {
		return cards.getBottom();
	}

	public Card getLastCard() {
		if (isEmpty())
			return null;
		return getAllCards().get(getSize() - 1);
	}

	@Override
	public int getIndexOfCard(Card card) {
		return cards.indexOf(card);
	}

	@Override
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	@Override
	public int getNumberOfHiddenCards() {
		return cards.getHiddenSize();
	}

	@Override
	public int getNumberOfVisibleCards() {
		return cards.getVisibleSize();
	}

	public boolean isCardVisible(Card card) {
		return getVisibleCards().contains(card);
	}

	public boolean containsCard(Card card) {
		return cards.contains(card);
	}

	@Override
	public int getSize() {
		return cards.getSize();
	}

	@Override
	public int getIndex() {
		return index;
	}

	void redact() {
		cards.redact();
	}

	@Override
	public MutableStack clone() {
		return new MutableStack(this);
	}

	Pile getPile() {
		return cards;
	}

	@Override
	public int compareTo(MutableStack that) {
		if (this.isEmpty() && that.isEmpty())
			return 0;
		else if (this.isEmpty())
			return -1;
		else if (that.isEmpty())
			return 1;
		else
			return this.cards.getAll().get(0).compareTo(that.cards.getAll().get(0));
	}

	@Override
	public int hashCode() {
		return cards.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableStack other = (MutableStack) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		}
		else if (!cards.equals(other.cards))
			return false;
		// if (index != other.index)
		// return false;
		return true;
	}

	@Override
	public String toString() {
		return "Stack " + cards;
	}
}
