package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;

import com.google.common.collect.AbstractIterator;

public abstract class AbstractGameSource extends AbstractIterator<Game> implements GameSource {
	protected final int size;
	protected final int handSize;

	public AbstractGameSource(int size, int handSize) {
		this.size = size;
		this.handSize = handSize;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int getHandSize() {
		return handSize;
	}
}
