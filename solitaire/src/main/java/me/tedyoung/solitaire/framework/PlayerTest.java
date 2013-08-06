package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.MutableGame;

public class PlayerTest implements PausableTest {
	private MutableGame game;

	private Player player;

	private int gameNumber;

	private PlayerTestStatistics statistics;

	public PlayerTest(MutableGame game, Player player, int gameNumber) {
		this.game = game;
		this.player = player;
		this.gameNumber = gameNumber;
	}

	@Override
	public void run() {
		synchronized (this) {
			statistics = new PlayerTestStatistics(player, gameNumber);
			statistics.startTimer();
		}
		GameResult result = player.playGame(game);
		synchronized (this) {
			statistics.stopTimer();
			statistics.setResult(result);
			statistics.setNumberOfMoves(game.getNumberOfMovesPlayed());
		}
	}

	@Override
	public void cleanup() {
		player.cleanup(game);
		game = null;
	}

	public PlayerTestStatistics getStatistics() {
		return statistics;
	}

	public Player getPlayer() {
		return player;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	@Override
	public synchronized void pause() {
		player.pause(game);
		if (statistics != null)
			statistics.stopTimer();
	}

	@Override
	public synchronized void resume() {
		player.resume(game);
		if (statistics != null)
			statistics.startTimer();
	}

	@Override
	public MutableGame getGame() {
		return game;
	}

}
