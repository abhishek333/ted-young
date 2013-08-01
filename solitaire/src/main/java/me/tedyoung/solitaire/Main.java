package me.tedyoung.solitaire;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import me.tedyoung.solitaire.framework.PlayerTestFactory;
import me.tedyoung.solitaire.framework.RandomGameSource;
import me.tedyoung.solitaire.framework.TestEngine;
import me.tedyoung.solitaire.mcs.MonteCarloSolver;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class Main {
	public static void main(String... arguments) throws InterruptedException, ExecutionException, IOException {
		TestEngine engine = new TestEngine(8);
		PlayerTestFactory factory = new PlayerTestFactory();
		// TesterTestFactory factory = new TesterTestFactory();
		engine.add(factory);

		// engine.setTranscriber(new FilesystemTranscriber("1000 Open 5 Close 1 TO 8 hrs", "../.."));

		// PersistentGameSource.save(new RandomGameSource(1000, 3), "../../games.ser");

		// factory.setGameSource(PersistentGameSource.load("../../test.ser"));

		RandomGameSource source = new RandomGameSource(100, 3);
		factory.setGameSource(source);
		System.out.println("Seed: " + source.getSeed() + "L");

		// engine.setTranscriber(new SerializingTranscriber("../../test.ser") {
		// @Override
		// public boolean includeGame(MutableGame game, Test test) {
		// PlayerTest playerTest = (PlayerTest) test;
		// return playerTest.getPlayer().getName().contains("DeadlockTester") && playerTest.getStatistics().getResult() == GameResult.WON;
		// }
		// });

		PlayerRunControl control = new PlayerRunControl(4, TimeUnit.HOURS, 5000);

		factory.add(new MonteCarloSolver(2, 1, control, false));
		factory.add(new MonteCarloSolver(2, 1, control, true));

		// factory.add(new MonteCarloSolver(null, 3, control, false));
		// factory.add(new MonteCarloSolver(3, 0, control, true));

		// factory.add(new DeadlockTester(control, true));
		// factory.add(new DeadlockTester(control, false));

		// factory.add(new ChainedPlayer("Chained -/3, 3/3", control, new MonteCarloSolver(null, 3, null), new MonteCarloSolver(3, 3, null)));

		// factory.add(new AdvancedPriorityPlayer());

		engine.run();

	}
}
