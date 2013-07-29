package me.tedyoung.solitaire.framework;

public interface ChainablePlayer extends Player {
	void chainedTo(Player player);
}
