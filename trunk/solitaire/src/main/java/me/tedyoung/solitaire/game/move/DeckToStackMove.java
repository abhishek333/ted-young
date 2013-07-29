package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.Stack;

public class DeckToStackMove implements Move {
	private Card card, target;

	private int stack;

	private int deals;

	private boolean flip;

	public DeckToStackMove(Card card, int stack, int deals, boolean flip) {
		this.card = card;
		this.stack = stack;
		this.deals = deals;
		this.flip = flip;
	}

	public DeckToStackMove(Card card, Stack stack, int deals, boolean flip) {
		this(card, stack.getIndex(), deals, flip);
	}

	public DeckToStackMove(Card card, int stack) {
		this(card, stack, -1, false);
	}

	public DeckToStackMove(Card card, Card parent) {
		this(card, -1, -1, false);
	}

	@Override
	public void apply(MutableGame game) {
		if (deals == -1) {
			game.getDeck().seek(card);
		}
		else {
			if (flip)
				game.getDeck().flip();

			for (int index = 0; index < deals; index++)
				game.getDeck().dealNextHand();
		}

		if (stack == -1) {
			for (MutableStack stack : game.getTable())
				if (stack.getAllCards().contains(target))
					this.stack = stack.getIndex();
		}

		game.getStack(stack).addCard(game.getDeck().removeCard(card));
	}

	@Override
	public Card getCard() {
		return card;
	}

	public int getStack() {
		return stack;
	}

	@Override
	public String toString() {
		return "DeckToStackMove [card=" + card + ", stack=" + stack + ", deals=" + deals + ", flip=" + flip + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
		result = prime * result + deals;
		result = prime * result + (flip ? 1231 : 1237);
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
		DeckToStackMove other = (DeckToStackMove) obj;
		if (card == null) {
			if (other.card != null)
				return false;
		}
		else if (!card.equals(other.card))
			return false;
		if (deals != other.deals)
			return false;
		if (flip != other.flip)
			return false;
		if (stack != other.stack)
			return false;
		return true;
	}

}
