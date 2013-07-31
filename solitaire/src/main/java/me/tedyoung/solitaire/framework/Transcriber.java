package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.MutableGame;

public interface Transcriber {
	void transcribe(MutableGame game, Test test);

	void start();

	void stop();
}
