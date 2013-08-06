package me.tedyoung.solitaire.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class ChainedPlayer extends AbstractPlayer {
	private String name = "Chained";

	private List<Player> players = new ArrayList<>();

	public ChainedPlayer(String name, PlayerRunControl control, Player... players) {
		this.name = name;
		setRunControl(control);
		this.players.addAll(Arrays.asList(players));

		for (Player player : players)
			player.setRunControl(control);

		for (int index = 0; index < players.length - 1; index++)
			if (players[index] instanceof ChainablePlayer)
				((ChainablePlayer) players[index]).chainedTo(players[index + 1]);

	}

	@Override
	public GameResult playGame(Game game) {
		GameResult result = GameResult.LOST;

		for (Player player : players) {
			((MutableGame) game).reset();
			result = player.playGame(game);
			if (result != GameResult.LOST)
				return result;
		}

		return result;
	}

	@Override
	public void cleanup(Game game) {
		for (Player player : players)
			player.cleanup(game);
	}

	@Override
	public String getName() {
		return name == null ? "Chained: " + players.get(players.size() - 1).getName() : name;
	}

}
