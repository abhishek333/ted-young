package me.tedyoung.solitaire.framework.heuristic;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public interface Heuristic<T> {
	int valueOf(Move move, Game game);
}
