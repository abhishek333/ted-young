package me.tedyoung.solitaire.game.move;

import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.Stack;

public class StackToStackMove implements Move {
	protected Card card, target;

	private int source, destination;

	public StackToStackMove(Card card, Stack source, Stack destination) {
		this(card, source.getIndex(), destination.getIndex());
	}

	public StackToStackMove(Card card, int source, int destination) {
		this.card = card;
		this.source = source;
		this.destination = destination;
	}

	public StackToStackMove(Card card, int destination) {
		this(card, -1, destination);
	}

	public StackToStackMove(Card card, Card target) {
		this(card, -1, -1);
		this.target = target;
	}

	@Override
	public void apply(MutableGame game) {
		if (source == -1) {
			for (MutableStack stack : game.getTable())
				if (stack.getAllCards().contains(card))
					this.source = stack.getIndex();
		}

		if (destination == -1) {
			for (MutableStack stack : game.getTable())
				if (stack.getAllCards().contains(target))
					this.destination = stack.getIndex();
		}

		List<Card> cards = game.getStack(source).removeCardsFrom(card);
		game.getStack(destination).addCards(cards);
	}

	@Override
	public Card getCard() {
		return card;
	}

	public int getSource() {
		return source;
	}

	public int getDestination() {
		return destination;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
		result = prime * result + destination;
		result = prime * result + source;
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
		StackToStackMove other = (StackToStackMove) obj;
		if (card == null) {
			if (other.card != null)
				return false;
		}
		else if (!card.equals(other.card))
			return false;
		if (destination != other.destination)
			return false;
		if (source != other.source)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StackToStackMove [card=" + card + ", source=" + source + ", destination=" + destination + "]";
	}

}
