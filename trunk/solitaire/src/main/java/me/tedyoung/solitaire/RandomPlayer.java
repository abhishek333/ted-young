package me.tedyoung.solitaire;

import java.util.List;

import me.tedyoung.solitaire.framework.AbstractIteratingPlayer;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;

public class RandomPlayer extends AbstractIteratingPlayer {

	@Override
	public Move chooseMove(Game game, List<Move> moves) {
		return moves.get((int) Math.random() * moves.size());
	}

	@Override
	public String getName() {
		return "10 Random";
	}

}
