package me.tedyoung.solitaire;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import me.tedyoung.solitaire.framework.GameResult;
import me.tedyoung.solitaire.framework.Player;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.mcs.MonteCarloSolver;
import me.tedyoung.solitaire.tester.DeadlockScanner;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class ProbabilityTests {
	private static final MathContext ctx = new MathContext(20, RoundingMode.HALF_UP);

	public static void main(String... args) {

		PlayerRunControl control = new PlayerRunControl(2, TimeUnit.MINUTES, 5000);
		Player player = new MonteCarloSolver(1, 0, control);

		while (true) {
			MutableGame game = new MutableGame();
			game.print(System.out);
			DeadlockScanner scanner = new DeadlockScanner(control, true);
			if (!scanner.isSolvable(game) && player.playGame(game) == GameResult.WON) {
				break;
			}

		}

	}
}
