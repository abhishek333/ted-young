package me.tedyoung.solitaire.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;

public class BasicMoveSource implements MoveSource {
	@Override
	public List<Move> getMoves(Game game) {
		ArrayList<Move> moves = new ArrayList<Move>(((MutableGame) game).getAllLegalMoves());

		Comparator<Move> comparator = getComparator(game);
		if (comparator != null)
			Collections.sort(moves, comparator);

		for (ListIterator<Move> iterator = moves.listIterator(); iterator.hasNext();)
			if (rejectMove(iterator.next(), game))
				iterator.remove();

		return moves;
	}

	protected boolean rejectMove(Move move, Game game) {
		return false;
	}

	protected Comparator<Move> getComparator(Game game) {
		return null;
	}
}
