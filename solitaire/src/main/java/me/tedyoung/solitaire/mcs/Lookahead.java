package me.tedyoung.solitaire.mcs;

import static java.lang.Math.random;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tedyoung.solitaire.framework.Abort;
import me.tedyoung.solitaire.framework.GameResult;
import me.tedyoung.solitaire.framework.heuristic.Heuristic;
import me.tedyoung.solitaire.framework.heuristic.HeuristicPlayer;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.SavedState;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.utilities.GameCache;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class Lookahead extends HeuristicPlayer {
	private final Lookahead lookahead;
	private final double randomness;

	private final GameCache<SavedState, Set<Move>> visited = new GameCache<SavedState, Set<Move>>(100_000) {
		@Override
		protected Set<Move> defaultValue() {
			return new HashSet<>();
		}
	};

	public Lookahead(Heuristic<?> heuristic, Lookahead lookahead) {
		this(heuristic, .5, lookahead);
	}

	public Lookahead(Heuristic<?> heuristic, double randomness, Lookahead lookahead) {
		super(heuristic);
		this.lookahead = lookahead;
		this.randomness = randomness;
		setMoveSource(new MonteCarloMoveSource());
	}

	@Override
	public GameResult playGame(Game g) {
		MutableGame game = (MutableGame) g;
		int mark = game.mark();
		GameResult result = super.playGame(game);
		if (result == GameResult.WON)
			throw new Abort();
		game.restore(mark);
		return result;
	}

	@Override
	public Move chooseMove(Game g, List<Move> moves) {
		MutableGame game = (MutableGame) g;
		if (lookahead != null)
			lookahead.playGame(game);
		Move move = super.chooseMove(game, moves);
		visited.get(game, game.getSavedState()).add(move);
		return move;
	}

	@Override
	public List<Move> getMoves(Game game) {
		List<Move> moves = super.getMoves(game);

		Set<Move> previouslyAttemptedMoves = visited.get(game, SavedState.proxy((MutableGame) game));
		for (Move move : previouslyAttemptedMoves)
			if (random() < randomness)
				moves.remove(move);

		return moves;
	}

	@Override
	public void setRunControl(PlayerRunControl control) {
		super.setRunControl(control);
		if (lookahead != null)
			lookahead.setRunControl(control);
	}

	@Override
	public String getName() {
		return heuristic.toString();
	}

}
