package me.tedyoung.solitaire.framework;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

import me.tedyoung.solitaire.game.MutableGame;

public class PlayerTestFactory implements TestFactory {
	protected IdentityHashMap<Player, PlayerStatistics> statistics = new IdentityHashMap<>();

	protected GameSource gameSource = new RandomGameSource(3, 1000);
	protected int numberOfGamesPlayed = 0;

	public PlayerTestFactory() {
	}

	public PlayerTestFactory(GameSource gameSource) {
		this.gameSource = gameSource;
	}

	public void add(Player... players) {
		for (Player player : players)
			statistics.put(player, new PlayerStatistics(player));
	}

	@Override
	public TestSource getTests() {
		final Set<Player> players = statistics.keySet();
		return new AbstractTestSource(gameSource.size() * players.size()) {
			Iterator<Player> iterator;
			MutableGame game;
			int gameNumber;

			@Override
			protected Test computeNext() {
				if (iterator == null || !iterator.hasNext()) {
					if (!gameSource.hasNext())
						return endOfData();
					iterator = players.iterator();
					game = (MutableGame) gameSource.next();
					gameNumber++;
				}
				return new PlayerTest(game.clone(), iterator.next(), gameNumber);
			}
		};
	}

	@Override
	public synchronized void testComplete(Test test) {
		PlayerTest playerTest = (PlayerTest) test;
		statistics.get(playerTest.getPlayer()).add(playerTest.getStatistics());
		numberOfGamesPlayed++;
	}

	@Override
	public synchronized void printSummary(PrintStream out, boolean abridged) {
		if (!abridged) {
			out.printf("Number of Games: %d, Cards per Draw: %d\n", numberOfGamesPlayed / statistics.size(), gameSource.getHandSize());
			out.printf("%-40s | %-16s | %-16s | %-17s | %-17s | %-15s\n", "Player", "Wins", "Aborts", "Avg Time", "Win/Loss Time", "Avg Moves");
		}
		else {
			out.printf("%-40s | %-16s | %-16s | %-17s | %-17s | %-15s | %-15s\n", "Player", "Wins", "Aborts", "Avg Time", "Win/Loss Time", "Avg Moves", "Played");
		}

		ArrayList<PlayerStatistics> results = new ArrayList<PlayerStatistics>(statistics.values());
		Collections.sort(results);

		HashMap<Player, ArrayList<Integer>> successes = new HashMap<>();

		double ms2s = 1_000.0;

		for (PlayerStatistics stats : results) {
			if (!abridged)
				out.printf("%-40s | %15.2f%% | %15.2f%% | %15.6f s | %7.1f/%7.1f s | %15.1f\n", stats.getPlayer().getName(), stats.getWinRatio() * 100, stats.getAbortedRatio() * 100, stats.getAverageTime() / ms2s, stats.getAverageWiningTime() / ms2s, stats.getAverageNonWiningTime() / ms2s, stats.getAverageNumberOfMoves());
			else
				out.printf("%-40s | %15.2f%% | %15.2f%% | %15.6f s | %7.1f/%7.1f s | %15.1f | %15d \n", stats.getPlayer().getName(), stats.getWinRatio() * 100, stats.getAbortedRatio() * 100, stats.getAverageTime() / ms2s, stats.getAverageWiningTime() / ms2s, stats.getAverageNonWiningTime() / ms2s, stats.getAverageNumberOfMoves(), stats.getNumberOfTests());

			if (!abridged)
				successes.put(stats.getPlayer(), stats.getCompleted());
		}

		if (!abridged && results.size() > 1) {
			out.println();
			out.println();

			out.printf("%-40s|", "");
			for (PlayerStatistics result : results) {
				Player player = result.getPlayer();
				out.printf("%-40s|", player.getName());
			}

			out.println();

			for (int index = 0; index < (successes.size() + 1) * 40 + results.size() + 1; index++)
				out.print("-");

			out.println();

			for (PlayerStatistics result : results) {
				Player player = result.getPlayer();
				out.printf("%-40s|", player.getName());
				for (PlayerStatistics otherResult : results) {
					Player competitor = otherResult.getPlayer();
					if (player == competitor) {
						out.printf("%-40s|", " ");
					}
					else {
						HashSet<Integer> completed = new HashSet<>(successes.get(player));
						completed.removeAll(successes.get(competitor));
						out.printf("%38.3f%% |", completed.size() / (double) gameSource.size() * 100);
					}
				}
				out.println();
			}
		}

	}

	public void setGameSource(GameSource gameSource) {
		this.gameSource = gameSource;
	}

}
