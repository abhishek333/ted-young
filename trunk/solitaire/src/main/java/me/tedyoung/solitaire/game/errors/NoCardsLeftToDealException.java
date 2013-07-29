package me.tedyoung.solitaire.game.errors;


public class NoCardsLeftToDealException extends RulesViolationException {
	private static final long serialVersionUID = 1L;

	public NoCardsLeftToDealException() {
		super("No cards left to deal from deck.");
	}
}
