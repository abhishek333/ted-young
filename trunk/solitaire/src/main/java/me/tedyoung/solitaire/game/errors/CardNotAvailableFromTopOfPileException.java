package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Stack;

public class CardNotAvailableFromTopOfPileException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public CardNotAvailableFromTopOfPileException(Stack pile, Card card) {
		super(String.format("Invalid attempt to remove card %s from top of pile %s.", card, pile));
	}
}
