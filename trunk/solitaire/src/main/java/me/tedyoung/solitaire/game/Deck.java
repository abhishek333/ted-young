package me.tedyoung.solitaire.game;

import java.util.List;

public interface Deck {

	int size();

	boolean isEmpty();

	boolean isFlippable();

	Card getTopCard();

	boolean hasNextHand();

	List<Card> getVisibleCards();

	int getNumberOfHandsRemaining();

	List<Card> getAllCards();

	int getHandSize();

}
