package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;

public class RandomGameSource extends AbstractGameSource {
	private int count;

	public RandomGameSource(int size, int handSize) {
		super(size, handSize);
	}

	@Override
	protected Game computeNext() {
		if (count++ == size)
			return endOfData();
		return new MutableGame(handSize);
	}
}
