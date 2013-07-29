package me.tedyoung.solitaire;

import java.util.List;

import me.tedyoung.solitaire.framework.AbstractScoringPlayer;
import me.tedyoung.solitaire.framework.heuristic.BasicMoveHeuristic;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public class BasicPriorityPlayer extends AbstractScoringPlayer {
	private static final BasicMoveHeuristic HEURISTIC = new BasicMoveHeuristic();

	@Override
	protected int score(Move move, Game game, List<Move> moves) {
		return HEURISTIC.valueOf(move, game);
	}

	@Override
	public String getName() {
		return "20 Basic Priority";
	}

}
