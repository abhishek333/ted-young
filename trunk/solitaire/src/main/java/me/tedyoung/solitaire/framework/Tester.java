package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public interface Tester {
	boolean isSolvable(Game game);

	String getName();

	void pause(Game game);

	void resume(Game game);

	void setRunControl(PlayerRunControl control);

	void cleanup(Game game);
}
