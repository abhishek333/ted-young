package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Foundation;

public class InvalidCardPlayedToFoundationException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public InvalidCardPlayedToFoundationException(Foundation foundation, Card card) {
		super(String.format("Invalid attempt to play card %s to foundation %s.", card, foundation));
	}
}
