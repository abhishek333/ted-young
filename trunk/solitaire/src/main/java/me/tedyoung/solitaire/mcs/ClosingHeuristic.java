package me.tedyoung.solitaire.mcs;

import java.util.ArrayList;
import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;

public class ClosingHeuristic extends MonteCarloHeuristic {
	public ClosingHeuristic(int evaluationDepth) {
		super("Close", evaluationDepth);
	}

	@Override
	public int valueOf(Game g) {
		MutableGame game = (MutableGame) g;

		if (game.isComplete())
			return WON;

		int score = game.getFoundation().size() * 5;

		for (MutableStack stack : game.getTable()) {
			List<Card> cards = new ArrayList<>(stack.getHiddenCards());

			for (Card card : cards)
				score += card.getDenomination().getValue() - 13;

			cards.addAll(0, stack.getVisibleCards());

			for (Card card : cards) {
				for (Card blocked : cards.subList(cards.indexOf(card) + 1, cards.size())) {
					if (card.isPeerOf(blocked))
						score += -1;

					if (blocked.isPredecessorOf(card))
						score += -1;

					if (blocked.isHolderOf(card))
						score += -5;
				}
			}
		}

		for (Card card : game.getDeck().getThisAndSubsequentVisibleCardsAndVisibleCardsAfterFlip())
			if (game.getFoundation().isPlayable(card) || game.getTable().isPlayable(card))
				score += 1;

		return score;
	}
}
