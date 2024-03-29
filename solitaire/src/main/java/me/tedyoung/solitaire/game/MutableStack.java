package me.tedyoung.solitaire.game;

import static java.util.Collections.unmodifiableList;
import static me.tedyoung.solitaire.game.Denomination.KING;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import me.tedyoung.solitaire.game.errors.CardNotAvailableFromTopOfPileException;
import me.tedyoung.solitaire.game.errors.CardNotFoundException;
import me.tedyoung.solitaire.game.errors.InvalidCardPlayedToStackException;

public class MutableStack implements Stack, Serializable {
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
		if (card == Card.UNKNOWN)
			return false;

		if (isEmpty())
			return card.getDenomination() == KING;

		Card topCard = getTopCard();
		if (topCard == Card.UNKNOWN)
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

		this.cards.add(Pile.LAST, true, cards);
	}

	@Override
	public List<Card> getVisibleCards() {
		return unmodifiableList(cards.getVisible());
	}

	public List<Card> getHiddenCards() {
		return unmodifiableList(cards.getHidden());
	}

	public List<Card> getAllCards() {
		return unmodifiableList(cards.getAll());
	}

	public List<Card> removeCardsFrom(Card card) {
		int index = getVisibleCards().indexOf(card);
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
		return cards.remove(0);
	}

	@Override
	public Card getTopCard() {
		return cards.getVisible().get(0);
	}

	@Override
	public Card getLastVisibleCard() {
		if (isEmpty())
			return null;
		List<Card> visibleCards = getVisibleCards();
		return visibleCards.get(visibleCards.size() - 1);
	}

	public Card getLastCard() {
		if (isEmpty())
			return null;
		return getAllCards().get(size() - 1);
	}

	@Override
	public int getNumberOfHiddenCards() {
		return cards.getHidden().size();
	}

	@Override
	public boolean isEmpty() {
		return cards.getAll().isEmpty();
	}

	@Override
	public int size() {
		return cards.getAll().size();
	}

	boolean contains(Card card) {
		return cards.getAll().contains(card);
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
