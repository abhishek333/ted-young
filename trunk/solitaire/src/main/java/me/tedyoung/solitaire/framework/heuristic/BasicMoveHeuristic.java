package me.tedyoung.solitaire.framework.heuristic;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.DealNextHandMove;
import me.tedyoung.solitaire.game.move.DeckToFoundationMove;
import me.tedyoung.solitaire.game.move.DeckToStackMove;
import me.tedyoung.solitaire.game.move.FlipDeckMove;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToFoundationMove;
import me.tedyoung.solitaire.game.move.StackToStackMove;

public class BasicMoveHeuristic implements MoveHeuristic {

	@Override
	public int valueOf(Move move, Game game) {
		if (move instanceof StackToFoundationMove)
			return 500;
		if (move instanceof DeckToFoundationMove)
			return 400;
		if (move instanceof DeckToStackMove)
			return 300;
		if (move instanceof StackToStackMove)
			return 200;
		if (move instanceof FoundationToStackMove)
			return -100;
		if (move instanceof DealNextHandMove)
			return 100;
		if (move instanceof FlipDeckMove)
			return 100;
		return 0;
	}

}
