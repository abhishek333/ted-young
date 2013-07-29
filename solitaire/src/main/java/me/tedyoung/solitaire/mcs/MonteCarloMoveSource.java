package me.tedyoung.solitaire.mcs;

import java.util.Comparator;
import java.util.List;

import me.tedyoung.solitaire.AdvancedPriorityPlayer;
import me.tedyoung.solitaire.framework.BasicMoveSource;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToStackMove;
import me.tedyoung.solitaire.utilities.CachingComparator;

public class MonteCarloMoveSource extends BasicMoveSource {
	// private boolean checkForDeadLocks = true;
	// private GameCache<SavedState, Boolean> deadlockCache = new GameCache<>(100_000);

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
				if (move.getCard().getDenomination().getValue() - foundation.getDenomination().getValue() > 3)
					return false;
			return true;

		}

		// if (checkForDeadLocks) {
		// if (move instanceof DeckToStackMove || move instanceof StackToStackMove || move instanceof FoundationToStackMove) {
		// if (game.getTable().getNumberOfHiddenCards() > 0) {
		// SavedState state = new SavedState(game, false);
		// Boolean deadlock = deadlockCache.getIfPresent(game, state);
		// if (deadlock == null) {
		// game.play(move);
		// deadlock = !new DeadlockFinder().isSolvable(game);
		// game.undo();
		// deadlockCache.set(game, state, deadlock);
		// }
		// if (deadlock)
		// return true;
		// }
		// }
		// }

		return false;
	}

	@Override
	protected Comparator<Move> getComparator(Game game) {
		return new MoveComparator(game);
	}

	private static final AdvancedPriorityPlayer player = new AdvancedPriorityPlayer();

	private static class MoveComparator extends CachingComparator<Move> {
		private final Game game;

		public MoveComparator(Game game) {
			this.game = game;
		}

		@Override
		protected int valueOf(Move move) {
			return -player.score(move, game, null);
		}
	}

	public void setCheckForDeadLocks(boolean checkForDeadLocks) {
		// this.checkForDeadLocks = checkForDeadLocks;
	}
}
