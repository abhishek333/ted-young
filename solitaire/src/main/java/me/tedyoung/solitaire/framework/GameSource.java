package me.tedyoung.solitaire.framework;

import java.util.Iterator;

import me.tedyoung.solitaire.game.Game;

public interface GameSource extends Iterator<Game> {
	int size();

	int getHandSize();
}
