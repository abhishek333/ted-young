package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;

public class CardNotAvailableFromTopOfFoundationException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public CardNotAvailableFromTopOfFoundationException(Card card) {
		super(String.format("Invalid attempt to remove card %s from foundation.", card));
	}
}
