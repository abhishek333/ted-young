package me.tedyoung.solitaire.game;

import java.util.List;

public interface Stack {

	boolean isPlayable(Card card);

	List<Card> getVisibleCards();

	Card getTopCard();

	int getIndexOfCard(Card card);

	boolean isEmpty();

	int getSize();

	int getIndex();

	int getNumberOfHiddenCards();

	int getNumberOfVisibleCards();

	Card getLastVisibleCard();

}
