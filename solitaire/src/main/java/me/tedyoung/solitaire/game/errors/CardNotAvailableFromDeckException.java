package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Deck;

public class CardNotAvailableFromDeckException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public CardNotAvailableFromDeckException(Deck deck, Card card) {
		super(String.format("Invalid attempt to remove card %s from deck %s.", card, deck));
	}

}
