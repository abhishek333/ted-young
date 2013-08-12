package me.tedyoung.solitaire.utilities;

import me.tedyoung.solitaire.game.Game;

public class NoOpPlayerRunControl extends PlayerRunControl {

	@Override
	public int getNumberOfMoves(Game game) {
		return super.getNumberOfMoves(game);
	}

	@Override
	public long getElapsedTime(Game game) {
		return 0;
	}

	@Override
	public void verify(Game game) {
	}

	@Override
	public void start(Game game) {
	}

	@Override
	public void stop(Game game) {
	}

	@Override
	public void pause(Game game) {
	}

	@Override
	public void resume(Game game) {
	}

	@Override
	public boolean isPaused(Game game) {
		return false;
	}

}
