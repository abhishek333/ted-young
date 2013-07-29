package me.tedyoung.solitaire.game.errors;

public class RulesViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RulesViolationException() {
		super();
	}

	public RulesViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RulesViolationException(String message) {
		super(message);
	}

	public RulesViolationException(Throwable cause) {
		super(cause);
	}
}
