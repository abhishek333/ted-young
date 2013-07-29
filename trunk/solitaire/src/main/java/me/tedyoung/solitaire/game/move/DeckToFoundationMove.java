package me.tedyoung.solitaire.game.move;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;

public class DeckToFoundationMove implements Move {

	private final Card card;

	private final int deals;

	private final boolean flip;

	public DeckToFoundationMove(Card card, int deals, boolean flip) {
		this.card = card;
		this.deals = deals;
		this.flip = flip;
	}

	public DeckToFoundationMove(Card card) {
		this(card, -1, false);
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

		game.getFoundation().addCard(game.getDeck().removeCard(card));
	}

	@Override
	public Card getCard() {
		return card;
	}

	@Override
	public String toString() {
		return "DeckToFoundationMove [card=" + card + ", deals=" + deals + ", flip=" + flip + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
		result = prime * result + deals;
		result = prime * result + (flip ? 1231 : 1237);
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
		DeckToFoundationMove other = (DeckToFoundationMove) obj;
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
		return true;
	}

}
