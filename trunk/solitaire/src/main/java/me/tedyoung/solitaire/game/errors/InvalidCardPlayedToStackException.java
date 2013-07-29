package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Stack;

public class InvalidCardPlayedToStackException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public InvalidCardPlayedToStackException(Stack stack, Card card) {
		super(String.format("Invalid attempt to play card %s to  %s.", card, stack));
	}

}
