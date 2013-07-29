package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.Stack;

public class StackToFoundationMove implements Move {
	protected final Card card;

	protected int stack;

	public StackToFoundationMove(Card card, Stack stack) {
		this(card, stack.getIndex());
	}

	public StackToFoundationMove(Card card, int stack) {
		this.card = card;
		this.stack = stack;
	}

	public StackToFoundationMove(Card card) {
		this(card, -1);
	}

	@Override
	public void apply(MutableGame game) {
		if (stack == -1) {
			for (MutableStack stack : game.getTable())
				if (stack.getAllCards().contains(card))
					this.stack = stack.getIndex();
		}

		game.getStack(stack).removeCard(card);
		game.getFoundation().addCard(card);
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
		StackToFoundationMove other = (StackToFoundationMove) obj;
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
		return "StackToFoundationMove [card=" + card + ", stack=" + stack + "]";
	}

}
