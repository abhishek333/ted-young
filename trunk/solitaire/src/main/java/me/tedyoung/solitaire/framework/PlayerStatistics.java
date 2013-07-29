package me.tedyoung.solitaire.framework;

import java.util.ArrayList;

public class PlayerStatistics implements Comparable<PlayerStatistics> {
	private Player player;

	private long completionTime;

	private long winingCompletionTime;

	private long numberOfMoves;

	private int wins;

	private int losses;

	private int aborted;

	private ArrayList<Integer> completed = new ArrayList<>();

	public PlayerStatistics(Player player) {
		this.player = player;
	}

	public void add(PlayerTestStatistics stats) {
		if (stats.getPlayer() != player)
			throw new RuntimeException("player mismatch");

		completionTime += stats.getCompletionTime();
		numberOfMoves += stats.getNumberOfMoves();

		if (stats.getResult() == GameResult.WON) {
			wins++;
			winingCompletionTime += stats.getCompletionTime();
			completed.add(stats.getGameNumber());
		}
		else if (stats.getResult() == GameResult.LOST) {
			losses++;
		}
		else {
			aborted++;
		}
	}

	public int getNumberOfTests() {
		return wins + losses + aborted;
	}

	public double getAverageTime() {
		return completionTime / (double) getNumberOfTests();
	}

	public double getAverageWiningTime() {
		return winingCompletionTime / (double) getWins();
	}

	public double getAverageNonWiningTime() {
		return (completionTime - winingCompletionTime) / (double) (getNumberOfTests() - getWins());
	}

	public long getCompletionTime() {
		return completionTime;
	}

	public int getWins() {
		return wins;
	}

	public double getWinRatio() {
		return wins / (double) getNumberOfTests();
	}

	public int getLosses() {
		return losses;
	}

	public int getAborted() {
		return aborted;
	}

	public double getAbortedRatio() {
		return aborted / (double) getNumberOfTests();
	}

	public long getNumberOfMoves() {
		return numberOfMoves;
	}

	public double getAverageNumberOfMoves() {
		return numberOfMoves / (double) getNumberOfTests();
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public int compareTo(PlayerStatistics that) {
		return this.player.getName().compareTo(that.player.getName());
	}

	public ArrayList<Integer> getCompleted() {
		return completed;
	}

	public long getWiningCompletionTime() {
		return winingCompletionTime;
	}

}
