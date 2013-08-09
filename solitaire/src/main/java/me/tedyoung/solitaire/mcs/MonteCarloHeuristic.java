package me.tedyoung.solitaire.mcs;

import me.tedyoung.solitaire.framework.heuristic.StateHeuristic;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;

public abstract class MonteCarloHeuristic implements StateHeuristic {
	public static final int WON = 1_000_000;

	public static final int LOST = -1_000_000;

	private int evaluationDepth;
	private String name;

	public MonteCarloHeuristic(String name, int evaluationDepth) {
		this.evaluationDepth = evaluationDepth;
		this.name = name;
	}

	@Override
	public int valueOf(Move move, Game game) {
		((MutableGame) game).play(move);
		int value = valueOf(game);
		((MutableGame) game).undo();
		return value;
	}

	public int getEvaluationDepth() {
		return evaluationDepth;
	}

	public void setEvaluationDepth(int evaluationDepth) {
		this.evaluationDepth = evaluationDepth;
	}

	@Override
	public String toString() {
		return name + "(" + evaluationDepth + ")";
	}

}
