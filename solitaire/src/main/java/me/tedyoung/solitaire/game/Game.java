package me.tedyoung.solitaire.game;

public interface Game {

	Deck getDeck();

	MutableTable getTable();

	Stack getStack(int index);

	boolean isComplete();

	int getNumberOfCards();

	boolean isInCycle();

	Foundation getFoundation();

	Game clone();

	MutableGame createPlayerCopy();

}
