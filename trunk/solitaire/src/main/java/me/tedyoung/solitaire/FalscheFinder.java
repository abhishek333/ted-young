package me.tedyoung.solitaire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.tedyoung.solitaire.framework.CollectionGameSource;
import me.tedyoung.solitaire.framework.GameResult;
import me.tedyoung.solitaire.framework.GameSource;
import me.tedyoung.solitaire.framework.PersistentGameSource;
import me.tedyoung.solitaire.framework.RandomGameSource;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.mcs.MonteCarloSolver;
import me.tedyoung.solitaire.tester.DeadlockTester;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class FalscheFinder {
	public static void main(String... args) throws IOException {
		PlayerRunControl control = new PlayerRunControl(4, TimeUnit.HOURS, 5000);
		DeadlockTester scanner = new DeadlockTester(control, false);
		MonteCarloSolver solver = new MonteCarloSolver(3, 2, control, false);

		int count = 2;

		ArrayList<Game> games = new ArrayList<>();
		GameSource source = new RandomGameSource(50, 3, -102389862848771009L);
		while (count > 0 && source.hasNext()) {
			Game game = source.next();
			if (!scanner.isSolvable(game) && solver.playGame(game) == GameResult.WON) {
				((MutableGame) game).reset();
				games.add(game);
				System.out.println(count--);
			}
		}

		PersistentGameSource.save(new CollectionGameSource(games), "../../games.ser");

	}
}
