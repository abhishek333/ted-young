package me.tedyoung.solitaire.framework;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class PlayerTestStatistics {
	private Player player;

	private Stopwatch stopwatch = new Stopwatch();

	private long numberOfMoves;

	private GameResult result;

	private int gameNumber;

	public PlayerTestStatistics(Player player, int gameNumber) {
		this.player = player;
		this.gameNumber = gameNumber;
	}

	public void startTimer() {
		if (!stopwatch.isRunning())
			stopwatch.start();
	}

	public void stopTimer() {
		if (stopwatch.isRunning())
			stopwatch.stop();
	}

	public GameResult getResult() {
		return result;
	}

	public void setResult(GameResult result) {
		this.result = result;
	}

	public long getCompletionTime() {
		return stopwatch.elapsed(TimeUnit.MILLISECONDS);
	}

	public Player getPlayer() {
		return player;
	}

	public long getNumberOfMoves() {
		return numberOfMoves;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setNumberOfMoves(long numberOfMoves) {
		this.numberOfMoves = numberOfMoves;
	}

}
