package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public abstract class AbstractTester implements Tester {
	private PlayerRunControl control = new PlayerRunControl();

	protected void startGame(Game game) {
		control.start(game);
	}

	protected void endGame(Game game) {
		control.stop(game);
	}

	@Override
	public void pause(Game game) {
		control.pause(game);
	}

	@Override
	public void resume(Game game) {
		control.resume(game);
	}

	protected void verify(Game game) {
		control.verify(game);
	}

	public PlayerRunControl getRunControl() {
		return control;
	}

	@Override
	public void setRunControl(PlayerRunControl control) {
		this.control = control;
	}
}
