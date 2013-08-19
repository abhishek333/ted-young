package me.tedyoung.solitaire.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import me.tedyoung.solitaire.framework.ui.SwingProgressBar;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.utilities.PausableThreadPoolExecutor;

public class TestEngine implements SwingProgressBar.Listener {
	private final ArrayList<TestFactory> factories = new ArrayList<>();

	private final PausableThreadPoolExecutor executor;
	private final Semaphore throttle;
	private final ConcurrentHashMap<Test, TestFactory> sources = new ConcurrentHashMap<>();
	private final SwingProgressBar progressBar;

	private Transcriber transcriber;

	public TestEngine() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public TestEngine(int threads) {
		progressBar = new SwingProgressBar(this);

		final int totalPermits = threads * 10;
		throttle = new Semaphore(totalPermits);
		executor = new PausableThreadPoolExecutor(threads) {
			@Override
			protected void afterExecute(Runnable runnable, Throwable t) {
				throttle.release();

				progressBar.setValue(getCompletedTaskCount() + 1, totalPermits - throttle.availablePermits(), getActiveCount());

				Test test = (Test) runnable;
				TestFactory factory = sources.remove(test);

				factory.testComplete(test);

				MutableGame game = test.getGame();
				test.cleanup();

				if (transcriber != null)
					transcriber.transcribe(game, test);
			}

			@Override
			protected void terminated() {
				progressBar.stop();

				if (transcriber != null)
					transcriber.stop();

				finalSummary();
			}
		};
	}

	public void run() throws InterruptedException, ExecutionException {
		int size = 0;

		if (transcriber != null)
			transcriber.start();

		progressBar.start();

		for (TestFactory factory : factories) {
			TestSource tests = factory.getTests();
			size += tests.size();
			progressBar.setMaximum(size);
			while (tests.hasNext()) {
				throttle.acquire();
				Test test = tests.next();
				sources.put(test, factory);
				executor.execute(test);
			}
		}

		executor.shutdown();
	}

	@Override
	public void pause() {
		executor.pause();
		for (Test test : sources.keySet())
			if (test instanceof PausableTest)
				((PausableTest) test).pause();
	}

	@Override
	public void resume() {
		for (Test test : sources.keySet())
			if (test instanceof PausableTest)
				((PausableTest) test).resume();
		executor.resume();
	}

	@Override
	public void summarize() {
		for (TestFactory factory : factories)
			factory.printSummary(System.out, true);
	}

	public void finalSummary() {
		for (TestFactory factory : factories)
			factory.printSummary(System.out, false);
	}

	public void add(TestFactory... factories) {
		this.factories.addAll(Arrays.asList(factories));
	}

	public void setTranscriber(Transcriber transcriber) {
		this.transcriber = transcriber;
	}
}
