package me.tedyoung.solitaire.game.errors;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Deck;
import me.tedyoung.solitaire.game.Stack;

public class CardNotFoundException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public CardNotFoundException(Deck deck, Card card) {
		super(String.format("Card %s was not found in deck %s.", card, deck));
	}

	public CardNotFoundException(Stack stack, Card card) {
		super(String.format("Card %s was not found in stack %s.", card, stack));
	}
}
