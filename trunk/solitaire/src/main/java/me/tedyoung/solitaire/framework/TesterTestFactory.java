package me.tedyoung.solitaire.framework;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class TesterTestFactory extends PlayerTestFactory {

	public TesterTestFactory() {
	}

	public TesterTestFactory(GameSource gameSource) {
		super(gameSource);
	}

	public void add(Tester... testers) {
		for (final Tester tester : testers) {
			Player player = new Player() {
				@Override
				public GameResult playGame(Game game) {
					try {
						return tester.isSolvable(game) ? GameResult.LOST : GameResult.WON;
					}
					catch (Abort abort) {
						return GameResult.ABORTED;
					}
				}

				@Override
				public void resume(Game game) {
					tester.resume(game);
				}

				@Override
				public void pause(Game game) {
					tester.pause(game);
				}

				@Override
				public String getName() {
					return tester.getName();
				}

				@Override
				public void setRunControl(PlayerRunControl control) {
					tester.setRunControl(control);
				}

				@Override
				public void cleanup(Game game) {
					tester.cleanup(game);
				}
			};
			super.add(player);
		}
	}

	@Override
	public void add(Player... players) {
		for (final Player player : players) {
			Player inverted = new Player() {
				@Override
				public GameResult playGame(Game game) {
					switch (player.playGame(game)) {
						case ABORTED:
							return GameResult.ABORTED;
						case LOST:
							return GameResult.WON;
						case WON:
							return GameResult.LOST;
						default:
							return null;
					}
				}

				@Override
				public void resume(Game game) {
					player.resume(game);
				}

				@Override
				public void pause(Game game) {
					player.pause(game);
				}

				@Override
				public String getName() {
					return player.getName();
				}

				@Override
				public void setRunControl(PlayerRunControl control) {
					player.setRunControl(control);
				}

				@Override
				public void cleanup(Game game) {
					player.cleanup(game);
				}
			};
			super.add(inverted);
		}
	}
}
