package me.tedyoung.solitaire.mcs;

import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;

public class OpeningHeuristic extends MonteCarloHeuristic {
	public OpeningHeuristic(int evaluationDepth) {
		super("Open", evaluationDepth);
	}

	@Override
	public int valueOf(Game g) {
		MutableGame game = (MutableGame) g;

		if (game.isComplete())
			return WON;

		int score = 0;

		for (Card card : game.getFoundation().getCards())
			score += 5 - card.getDenomination().getValue();

		for (MutableStack stack : game.getTable()) {
			List<Card> cards = stack.getHiddenCards();

			for (Card card : cards)
				score += card.getDenomination().getValue() - 13;

			cards = stack.getAllCards();

			for (Card card : cards) {
				for (Card blocked : cards.subList(cards.indexOf(card) + 1, cards.size())) {
					if (card.isPeerOf(blocked))
						score += -5;

					if (blocked.isPredecessorOf(card))
						score += -5;

					if (blocked.isHolderOf(card))
						score += -10;
				}
			}
		}

		return score;
	}
}
