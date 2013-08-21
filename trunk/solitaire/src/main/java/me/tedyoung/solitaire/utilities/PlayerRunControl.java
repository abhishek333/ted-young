package me.tedyoung.solitaire.utilities;

import java.util.concurrent.TimeUnit;

import me.tedyoung.solitaire.framework.Abort;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;

import com.google.common.base.Stopwatch;

public class PlayerRunControl {
	private final RunControl runControl = new RunControl();

	private final long maximumTime;
	private final GameLocal<Stopwatch> stopwatch = new GameLocal<Stopwatch>() {
		@Override
		protected Stopwatch defaultValue() {
			return new Stopwatch();
		}
	};

	private final GameLocal<Boolean> abort = new GameLocal<Boolean>(false);

	private final long maximumMoves;

	public PlayerRunControl() {
		this(0, null, 0);
	}

	public PlayerRunControl(long maximumTime, TimeUnit unit, long maximumMoves) {
		this.maximumTime = maximumTime == 0 ? 0 : unit.toMillis(maximumTime);
		this.maximumMoves = maximumMoves;
	}

	public int getNumberOfMoves(Game game) {
		return ((MutableGame) game).getNumberOfMovesPlayed();
	}

	public long getElapsedTime(Game game) {
		return stopwatch.get(game).elapsed(TimeUnit.MILLISECONDS);
	}

	public void verify(Game game) {
		if (maximumTime > 0)
			if (getElapsedTime(game) > maximumTime)
				throw new Abort.Timeout();

		if (maximumMoves > 0)
			if (getNumberOfMoves(game) > maximumMoves)
				throw new Abort.Distance();

		if (abort.get(game))
			throw new Abort.User();

		runControl.verify();
	}

	public void start(Game game) {
		Stopwatch watch = stopwatch.get(game);
		if (!watch.isRunning())
			watch.start();
	}

	public void stop(Game game) {
		Stopwatch watch = stopwatch.get(game);
		if (watch.isRunning())
			watch.stop();
	}

	public void pause(Game game) {
		Stopwatch watch = stopwatch.get(game);
		if (watch.isRunning())
			watch.stop();
		runControl.pause();
	}

	public void resume(Game game) {
		runControl.resume();
		Stopwatch watch = stopwatch.get(game);
		if (!watch.isRunning())
			watch.start();
	}

	public void abort(Game game) {
		abort.set(game, true);
	}

	public boolean isPaused(Game game) {
		return runControl.isPaused();
	}
}
