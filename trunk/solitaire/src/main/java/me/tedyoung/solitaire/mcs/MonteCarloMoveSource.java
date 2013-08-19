package me.tedyoung.solitaire.mcs;

import java.util.Comparator;
import java.util.List;

import me.tedyoung.solitaire.framework.BasicMoveSource;
import me.tedyoung.solitaire.framework.heuristic.AdvancedMoveHeuristic;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToStackMove;
import me.tedyoung.solitaire.utilities.CachingComparator;

public class MonteCarloMoveSource extends BasicMoveSource {
	@SuppressWarnings("unused")
	private boolean revised;

	public MonteCarloMoveSource() {
	}

	public MonteCarloMoveSource(boolean revised) {
		this.revised = revised;
	}

	@Override
	protected boolean rejectMove(Move move, Game g) {
		MutableGame game = (MutableGame) g;

		if (move instanceof StackToStackMove) {
			MutableStack stack = game.getStack(((StackToStackMove) move).getSource());
			if (move.getCard().equals(stack.getLastVisibleCard()))
				return false;

			List<Card> cards = stack.getAllCards();
			int index = cards.indexOf(move.getCard());
			if (index < cards.size() - 1 && game.getFoundation().isPlayable(cards.get(index + 1)))
				return false;

			return true;
		}

		if (move instanceof FoundationToStackMove) {
			if (move.getCard().getDenomination().getValue() <= 2)
				return true;

			for (Card foundation : game.getFoundation().getTopCards())
				if (move.getCard().getDenomination().getValue() - foundation.getDenomination().getValue() > 2)
					return false;
			return true;
		}

		return false;
	}

	@Override
	protected Comparator<Move> getComparator(Game game) {
		return new MoveComparator(game);
	}

	private static final AdvancedMoveHeuristic HEURISTIC = new AdvancedMoveHeuristic();

	private static class MoveComparator extends CachingComparator<Move> {
		private final Game game;

		public MoveComparator(Game game) {
			this.game = game;
		}

		@Override
		protected int valueOf(Move move) {
			return -HEURISTIC.valueOf(move, game);
		}
	}
}
