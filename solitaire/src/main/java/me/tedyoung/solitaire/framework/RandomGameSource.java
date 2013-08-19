package me.tedyoung.solitaire.framework;

import java.security.SecureRandom;
import java.util.Random;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;

public class RandomGameSource extends AbstractGameSource {
	private final long seed;
	private final Random random;
	private int count;

	public RandomGameSource(int size, int handSize) {
		this(size, handSize, new SecureRandom().nextLong());
	}

	public RandomGameSource(int size, int handSize, long seed) {
		super(size, handSize);
		this.seed = seed;
		this.random = new SecureRandom();
		this.random.setSeed(seed);
	}

	@Override
	protected Game computeNext() {
		if (count++ == size)
			return endOfData();
		return new MutableGame(handSize, random);
	}

	public long getSeed() {
		return seed;
	}
}
