package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;

public class FlipDeckMove implements Move {
	@Override
	public void apply(MutableGame game) {
		game.getDeck().flip();
	}

	@Override
	public Card getCard() {
		return null;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlipDeck []";
	}

}
