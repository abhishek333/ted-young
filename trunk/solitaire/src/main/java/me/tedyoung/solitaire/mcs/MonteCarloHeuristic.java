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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + evaluationDepth;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonteCarloHeuristic other = (MonteCarloHeuristic) obj;
		if (evaluationDepth != other.evaluationDepth)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + "(" + evaluationDepth + ")";
	}

}
