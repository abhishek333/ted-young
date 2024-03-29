package me.tedyoung.solitaire.framework;

import java.util.Random;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;

public class RandomGameSource extends AbstractGameSource {
	private final long seed;
	private final Random random;
	private int count;

	public RandomGameSource(int size, int handSize) {
		this(size, handSize, new Random().nextLong());
	}

	public RandomGameSource(int size, int handSize, long seed) {
		super(size, handSize);
		this.seed = seed;
		this.random = new Random(seed);
	}

	public RandomGameSource(int size, int handSize, Random random) {
		super(size, handSize);
		this.seed = 0;
		this.random = random;
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
