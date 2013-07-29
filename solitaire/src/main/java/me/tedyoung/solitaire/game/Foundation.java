package me.tedyoung.solitaire.game;

import java.util.List;

public interface Foundation {

	boolean isPlayable(Card card);

	boolean isEmpty();

	boolean isComplete();

	int getSize();

	Card getTopCard(Suit suit);

	Denomination getCurrentDenomination(Suit suit);

	boolean isEmpty(Suit suit);

	boolean isComplete(Suit suit);

	int getSize(Suit suit);

	List<Card> getTopCards();

}
