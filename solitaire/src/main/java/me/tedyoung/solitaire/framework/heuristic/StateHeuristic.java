package me.tedyoung.solitaire.framework.heuristic;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public interface StateHeuristic extends Heuristic<Move> {
	int valueOf(Game game);
}
