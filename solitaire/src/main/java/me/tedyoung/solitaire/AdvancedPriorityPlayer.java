package me.tedyoung.solitaire;

import java.util.List;

import me.tedyoung.solitaire.framework.AbstractScoringPlayer;
import me.tedyoung.solitaire.framework.heuristic.AdvancedMoveHeuristic;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public class AdvancedPriorityPlayer extends AbstractScoringPlayer {
	private static final AdvancedMoveHeuristic HEURISTIC = new AdvancedMoveHeuristic();

	public AdvancedPriorityPlayer() {
		chooseLast = true;
	}

	@Override
	public int score(Move move, Game game, List<Move> moves) {
		return HEURISTIC.valueOf(move, game);
	}

	@Override
	public String getName() {
		return "30 Advanced Priority Mod";
	}

}
