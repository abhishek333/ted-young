package me.tedyoung.solitaire.game;

import java.util.List;

public interface Stack {

	boolean isPlayable(Card card);

	List<Card> getVisibleCards();

	Card getTopCard();

	boolean isEmpty();

	int size();

	int getIndex();

	Card getLastVisibleCard();

	int getNumberOfHiddenCards();

}
