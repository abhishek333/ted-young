package me.tedyoung.solitaire.mcs;

import me.tedyoung.solitaire.framework.heuristic.StateHeuristic;
import me.tedyoung.solitaire.game.StateKey;

public class CacheEntry {
	private final StateKey state;
	private final StateHeuristic heuristic;
	private final int level;

	public CacheEntry(StateKey state, MonteCarloHeuristic heuristic, int level) {
		this.state = state;
		this.heuristic = heuristic;
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((heuristic == null) ? 0 : heuristic.hashCode());
		result = prime * result + level;
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
		CacheEntry other = (CacheEntry) obj;
		if (heuristic == null) {
			if (other.heuristic != null)
				return false;
		}
		else if (!heuristic.equals(other.heuristic))
			return false;
		if (level != other.level)
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
