package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;

public class DealNextHandMove implements Move {

	private final int index;

	public DealNextHandMove(int index) {
		this.index = index;
	}

	public DealNextHandMove() {
		this(1);
	}

	@Override
	public void apply(MutableGame game) {
		for (int index = 0; index < this.index; index++)
			game.getDeck().dealNextHand();
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
		return "DealNextHand []";
	}
}
