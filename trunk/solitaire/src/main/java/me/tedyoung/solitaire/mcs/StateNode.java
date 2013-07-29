package me.tedyoung.solitaire.mcs;

import me.tedyoung.solitaire.game.SavedState;
import me.tedyoung.solitaire.game.move.Move;

public class StateNode {
	private final SavedState state;
	private final Move move;

	public StateNode(SavedState state, Move move) {
		this.state = state;
		this.move = move;
	}

	public SavedState getState() {
		return state;
	}

	public Move getMove() {
		return move;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		StateNode other = (StateNode) obj;
		if (move == null) {
			if (other.move != null)
				return false;
		}
		else if (!move.equals(other.move))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		}
		else if (!state.equals(other.state))
			return false;
		return true;
	}

}
