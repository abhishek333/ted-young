package me.tedyoung.solitaire.framework;

import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;

public abstract class AbstractIteratingPlayer extends AbstractPlayer {
	private MoveSource moveSource = new BasicMoveSource();

	@Override
	public GameResult playGame(Game g) {
		MutableGame game = (MutableGame) g;

		start(game);

		try {
			for (;;) {
				verify(game);

				if (game.isComplete())
					return GameResult.WON;

				List<Move> moves = getMoves(game);

				if (moves.isEmpty())
					return GameResult.LOST;

				Move move = chooseMove(game, moves);
				verify(game);

				if (move == null)
					return GameResult.LOST;

				game.play(move);
			}
		}
		catch (Abort abort) {
			return game.isComplete() ? GameResult.WON : GameResult.ABORTED;
		}
		finally {
			stop(game);
		}
	}

	protected List<Move> getMoves(Game game) {
		return moveSource.getMoves(game);
	}

	protected Move chooseMove(Game game, List<Move> moves) {
		return null;
	}

	public void setMoveSource(MoveSource moveSource) {
		this.moveSource = moveSource;
	}

}
