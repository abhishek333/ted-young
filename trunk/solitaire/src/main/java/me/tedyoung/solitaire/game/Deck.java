package me.tedyoung.solitaire.game;

import java.util.List;

public interface Deck {

	int getSize();

	boolean isEmpty();

	boolean isFlippable();

	Card getTopCard();

	boolean hasNextHand();

	int getIndexOfCard(Card card);

	List<Card> getVisibleCards();

	int getNumberOfVisibleCards();

	int getNumberOfHandsRemaining();

	List<Card> getAllCards();

	int getHandSize();

}
