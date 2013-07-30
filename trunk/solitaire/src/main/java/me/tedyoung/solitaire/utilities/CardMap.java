package me.tedyoung.solitaire.utilities;

import java.util.EnumMap;

import me.tedyoung.solitaire.game.Card;

public class CardMap<V> extends EnumMap<Card, V> {
	private static final long serialVersionUID = 1L;

	public CardMap() {
		super(Card.class);
	}

}
