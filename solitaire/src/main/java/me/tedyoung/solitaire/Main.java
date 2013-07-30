package me.tedyoung.solitaire;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import me.tedyoung.solitaire.framework.RandomGameSource;
import me.tedyoung.solitaire.framework.TestEngine;
import me.tedyoung.solitaire.framework.TesterTestFactory;
import me.tedyoung.solitaire.mcs.MonteCarloSolver;
import me.tedyoung.solitaire.tester.DeadlockScanner;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class Main {
	public static void main(String... arguments) throws InterruptedException, ExecutionException, IOException {
		TestEngine engine = new TestEngine(6);
		// PlayerTestFactory factory = new PlayerTestFactory();
		TesterTestFactory factory = new TesterTestFactory();
		engine.add(factory);

		// PersistentGameSource.save(new RandomGameSource(1000, 3), "../../games.ser");

		// factory.setGameSource(PersistentGameSource.load("../../games.ser"));

		RandomGameSource source = new RandomGameSource(1000, 3);
		factory.setGameSource(source);
		System.out.println("Seed: " + source.getSeed() + "L");

		// engine.setTranscriber(new FilesystemTranscriber("1000 Open 5 Close 1 TO 8 hrs", "../.."));

		PlayerRunControl control = new PlayerRunControl(120, TimeUnit.MINUTES, 5000);
		// // AbstractPlayer.TRACE = true;

		// factory.add(new DeadLockDetector());

		factory.add(new MonteCarloSolver(2, 1, control, false));
		factory.add(new DeadlockScanner(control, true));
		factory.add(new DeadlockScanner(control, false));

		// factory.add(new ChainedPlayer("Chained -/3, 3/1", new MonteCarloSolver(null, 3, timeout), new MonteCarloSolver(3, 1, timeout)));
		// factory.add(new ChainedPlayer("Chained -/3, 5/1*", new MonteCarloSolver(null, 3, timeout, true), new MonteCarloSolver(5, 1, timeout, true)));

		// factory.add(new MonteCarloSolver(timeout, new OpeningHeuristic(1), new ClosingHeuristic(1)));

		// factory.add(new AdvancedPriorityPlayer());

		engine.run();

	}
}
