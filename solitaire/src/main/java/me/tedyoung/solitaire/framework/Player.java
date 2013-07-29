package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public interface Player {
	GameResult playGame(Game game);

	String getName();

	void pause(Game game);

	void resume(Game game);

	void setRunControl(PlayerRunControl control);
}
