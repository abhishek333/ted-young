package me.tedyoung.solitaire.framework;

import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public abstract class AbstractScoringPlayer extends AbstractIteratingPlayer {

	protected boolean chooseLast = false;

	@Override
	public Move chooseMove(Game game, List<Move> moves) {
		int highScore = Integer.MIN_VALUE;
		Move bestMove = null;

		for (Move move : moves) {
			int score = score(move, game, moves);
			if (chooseLast ? score >= highScore : score > highScore) {
				highScore = score;
				bestMove = move;
			}
		}

		return bestMove;
	}

	protected abstract int score(Move move, Game game, List<Move> moves);
}
