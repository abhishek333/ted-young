package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.Stack;

public class FoundationToStackMove implements Move {
	private final Card card;

	private final int stack;

	public FoundationToStackMove(Card card, Stack stack) {
		this(card, stack.getIndex());
	}

	public FoundationToStackMove(Card card, int stack) {
		this.card = card;
		this.stack = stack;
	}

	@Override
	public void apply(MutableGame game) {
		game.getStack(stack).addCard(game.getFoundation().removeCard(card));
	}

	@Override
	public Card getCard() {
		return card;
	}

	public int getStack() {
		return stack;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
		result = prime * result + stack;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoundationToStackMove other = (FoundationToStackMove) obj;
		if (card == null) {
			if (other.card != null)
				return false;
		}
		else if (!card.equals(other.card))
			return false;
		if (stack != other.stack)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FoundationToStackMove [card=" + card + ", stack=" + stack + "]";
	}
}
