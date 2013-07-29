package me.tedyoung.solitaire.framework;

import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public interface MoveSource {
	List<Move> getMoves(Game game);
}
