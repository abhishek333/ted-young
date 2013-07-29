package me.tedyoung.solitaire.framework;

import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;

public abstract class AbstractIteratingPlayer extends AbstractPlayer {
	public static boolean TRACE = false;

	private MoveSource moveSource = new BasicMoveSource();

	@Override
	public GameResult playGame(Game g) {
		MutableGame game = (MutableGame) g;

		startGame(game);

		if (TRACE)
			game.print(System.out);

		try {
			for (;;) {
				verify(game);

				if (game.isComplete()) {
					if (TRACE)
						System.out.println("Win!");
					return GameResult.WON;
				}

				List<Move> moves = getMoves(game);

				if (TRACE)
					System.out.println("\n" + moves);

				if (moves.isEmpty()) {
					if (TRACE)
						System.out.println("Lose!");
					return GameResult.LOST;
				}

				Move move = chooseMove(game, moves);
				verify(game);

				if (TRACE)
					System.out.println(move + "\n");

				if (move == null) {
					if (TRACE)
						System.out.println("Gave up!");
					return GameResult.LOST;
				}

				game.play(move);

				if (TRACE)
					game.print(System.out);
			}
		}
		catch (Abort abort) {
			endGame(game);
			return game.isComplete() ? GameResult.WON : GameResult.ABORTED;
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
