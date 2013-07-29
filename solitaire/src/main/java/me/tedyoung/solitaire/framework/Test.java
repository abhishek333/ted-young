package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.MutableGame;

public interface Test extends Runnable {
	MutableGame getGame();

	void cleanup();
}
