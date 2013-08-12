package me.tedyoung.solitaire.framework.heuristic;

import java.util.List;

import me.tedyoung.solitaire.framework.AbstractScoringPlayer;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.utilities.NoOpPlayerRunControl;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class HeuristicPlayer extends AbstractScoringPlayer {
	protected final Heuristic<?> heuristic;

	protected String name;

	public HeuristicPlayer(String name, PlayerRunControl runControl, Heuristic<?> heuristic) {
		this.heuristic = heuristic;
		this.name = name;
		setRunControl(runControl);
	}

	public HeuristicPlayer(PlayerRunControl runControl, Heuristic<?> heuristic) {
		this("", runControl, heuristic);
	}

	public HeuristicPlayer(Heuristic<?> heuristic) {
		this("", new NoOpPlayerRunControl(), heuristic);
	}

	@Override
	protected int score(Move move, Game game, List<Move> moves) {
		return heuristic.valueOf(move, game);
	}

	public Heuristic<?> getHeuristic() {
		return heuristic;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + "-" + heuristic;
	}

}
