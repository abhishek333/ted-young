package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Stack;

public class AttemptToRemoveHiddenCardException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public AttemptToRemoveHiddenCardException(Stack pile, Card card) {
		super(String.format("Attempt to remove hidden card %s from pile %s.", card, pile));
	}
}
