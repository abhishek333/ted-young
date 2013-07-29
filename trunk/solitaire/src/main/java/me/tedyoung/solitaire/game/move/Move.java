package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;

public interface Move {
	void apply(MutableGame game);

	Card getCard();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);
}
