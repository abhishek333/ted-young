package me.tedyoung.solitaire.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tedyoung.solitaire.game.errors.CardNotAvailableFromDeckException;

public class MutableDeck implements Deck, Serializable {
	private static final long serialVersionUID = 1L;

	private final int handSize;
	private Pile cards;

	public MutableDeck() {
		this(3);
	}

	public MutableDeck(int handSize) {
		this.handSize = handSize;

		List<Card> cards = new ArrayList<>(Card.getAll());
		Collections.shuffle(cards);

		this.cards = new Pile();
		this.cards.add(0, false, cards);

		dealNextHand();
	}

	MutableDeck(MutableDeck deck) {
		this.cards = deck.cards.clone();
		this.handSize = deck.handSize;
	}

	@Override
	public Card getTopCard() {
		// Don't do this; it is a violation of immutability.
		// if (!cards.isAnyVisible())
		// dealNextHand();

		return cards.getTop();
	}

	@Override
	public List<Card> getVisibleCards() {
		return Collections.unmodifiableList(cards.getVisible());
	}

	public List<Card> getHiddenCards() {
		return Collections.unmodifiableList(cards.getHidden());
	}

	public Card removeTopCard() {
		return cards.removeTop();
	}

	public Card removeCard(Card card) {
		if (!getTopCard().equals(card))
			throw new CardNotAvailableFromDeckException(this, card);

		return removeTopCard();
	}

	public void dealNextHand() {
		cards.show(handSize);
	}

	public void seek(Card card) {
		while (hasNextHand() && !getTopCard().equals(card))
			dealNextHand();
		if (!getTopCard().equals(card)) {
			flip();
			while (hasNextHand() && !getTopCard().equals(card))
				dealNextHand();
		}
		if (!getTopCard().equals(card))
			throw new CardNotAvailableFromDeckException(this, card);
	}

	@Override
	public boolean hasNextHand() {
		return cards.isAnyHidden();
	}

	@Override
	public int getNumberOfHandsRemaining() {
		return (int) Math.ceil(cards.getHiddenSize() / (double) handSize);
	}

	public void flip() {
		cards.hideAll();
		dealNextHand();
	}

	@Override
	public List<Card> getAllCards() {
		ArrayList<Card> list = new ArrayList<>(cards.getAll());
		Collections.reverse(list);
		return list;
	}

	@Override
	public boolean isFlippable() {
		return getSize() > handSize;
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
	public int getSize() {
		return cards.getSize();
	}

	@Override
	public int getNumberOfVisibleCards() {
		return cards.getVisibleSize();
	}

	public List<List<Card>> getHands() {
		return cards.group(handSize);
	}

	public List<Card> getThisAndSubsequentVisibleCards() {
		ArrayList<Card> hands = new ArrayList<>();

		int previousTopCardIndex = cards.getFirstVisibleCardIndex();

		if (getNumberOfVisibleCards() != 0)
			hands.add(getTopCard());

		while (hasNextHand()) {
			dealNextHand();
			hands.add(getTopCard());
		}

		cards.setFirstVisibleCardIndex(previousTopCardIndex);

		return hands;
	}

	public List<Card> getSubsequentVisibleCardsAfterFlip() {
		int previousTopCardIndex = cards.getFirstVisibleCardIndex();

		flip();
		List<Card> hands = getThisAndSubsequentVisibleCards();

		cards.setFirstVisibleCardIndex(previousTopCardIndex);

		return hands;
	}

	public Set<Card> getThisAndSubsequentVisibleCardsAndVisibleCardsAfterFlip() {
		HashSet<Card> cards = new HashSet<Card>();
		cards.addAll(getThisAndSubsequentVisibleCards());
		cards.addAll(getSubsequentVisibleCardsAfterFlip());
		return cards;
	}

	public boolean containsCard(Card card) {
		return cards.contains(card);
	}

	@Override
	public int getHandSize() {
		return handSize;
	}

	List<Card> deal(int count) {
		return cards.deal(count);
	}

	void redact() {
		cards.redact();
	}

	Pile getCards() {
		return cards;
	}

	@Override
	public MutableDeck clone() {
		return new MutableDeck(this);
	}

	@Override
	public String toString() {
		return "Deck " + cards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cards == null) ? 0 : cards.hashCode());
		result = prime * result + handSize;
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
		MutableDeck other = (MutableDeck) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		}
		else if (!cards.equals(other.cards))
			return false;
		if (handSize != other.handSize)
			return false;
		return true;
	}

}
