package me.tedyoung.solitaire.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;

public class ChainedPlayer extends AbstractPlayer {
	private String name = "Chained";

	private List<Player> players = new ArrayList<>();

	public ChainedPlayer(String name, Player... players) {
		this.name = name;
		this.players.addAll(Arrays.asList(players));

		for (int index = players.length - 1; index > 0; index--)
			if (players[index - 1] instanceof ChainablePlayer)
				((ChainablePlayer) players[index - 1]).chainedTo(players[index]);
	}

	@Override
	public GameResult playGame(Game game) {
		GameResult result = GameResult.LOST;

		for (Player player : players) {
			((MutableGame) game).reset();
			result = player.playGame(game);
			if (result == GameResult.WON)
				return GameResult.WON;
		}

		return result;
	}

	@Override
	public void pause(Game game) {
		for (Player player : players)
			player.pause(game);
	}

	@Override
	public void resume(Game game) {
		for (Player player : players)
			player.resume(game);
	}

	@Override
	public String getName() {
		return name;
	}

}
