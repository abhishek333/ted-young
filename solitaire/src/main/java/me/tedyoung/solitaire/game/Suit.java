package me.tedyoung.solitaire.game;

import static me.tedyoung.solitaire.game.Color.BLACK;
import static me.tedyoung.solitaire.game.Color.RED;

import java.util.ArrayList;
import java.util.List;

public enum Suit {
	SPADES("\u2660", BLACK), HEARTS("\u2665", RED), CLUBS("\u2663", BLACK), DIAMONDS("\u2666", RED);

	private final String symbol;

	private final Color color;

	private Suit(String symbol, Color color) {
		this.symbol = symbol;
		this.color = color;
	}

	public List<Suit> getSuitsNotThisColor() {
		ArrayList<Suit> list = new ArrayList<>(2);
		for (Suit suit : values())
			if (!isSameColor(suit))
				list.add(suit);
		return list;
	}

	public Suit getOtherSuitThisColor() {
		for (Suit suit : values())
			if (suit != this && isSameColor(suit))
				return suit;
		return null;
	}

	public boolean isSameColor(Suit that) {
		return this.color == that.color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
