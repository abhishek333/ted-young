package me.tedyoung.solitaire.mcs;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.StateKey;
import me.tedyoung.solitaire.utilities.GameCache;

public class Cache {
	private boolean revised;
	private GameCache<Key, Integer> longCache = new GameCache<>(1_000_000, Integer.MIN_VALUE);
	private GameCache<Key, Integer> shortCache = new GameCache<>(2_000_000, Integer.MIN_VALUE);

	private Cache sharedCache;
	private List<Class<?>> sharedHeuristics = new ArrayList<>();

	public Cache() {
		this(false);
	}

	public Cache(boolean revised) {
		this.revised = revised;
	}

	public void share(List<MonteCarloHeuristic> owner, Cache otherCache, List<MonteCarloHeuristic> other) {
		ListIterator<MonteCarloHeuristic> ownerHeuristics = owner.listIterator(owner.size());
		ListIterator<MonteCarloHeuristic> otherHeuristics = other.listIterator(owner.size());

		while (ownerHeuristics.hasPrevious() && otherHeuristics.hasPrevious()) {
			MonteCarloHeuristic ownerHeuristic = ownerHeuristics.previous();
			MonteCarloHeuristic otherHeuristic = otherHeuristics.previous();

			if (ownerHeuristic.getClass() == otherHeuristic.getClass())
				sharedHeuristics.add(ownerHeuristic.getClass());
			else
				break;

			if (ownerHeuristic.getEvaluationDepth() != otherHeuristic.getEvaluationDepth())
				break;
		}

		if (!sharedHeuristics.isEmpty())
			this.sharedCache = otherCache;
	}

	public Integer get(MutableGame game, StateKey state, MonteCarloHeuristic heuristic, int level) {
		Key key = new Key(state, heuristic, level);
		GameCache<Key, Integer> cache = cache(key);
		if (cache == null)
			return null;
		Integer result = cache.get(game, key);
		return result == Integer.MIN_VALUE ? null : result;
	}

	public void set(MutableGame game, StateKey state, MonteCarloHeuristic heuristic, int level, int value) {
		Key key = new Key(state, heuristic, level);
		set(game, heuristic, key, value);
	}

	private void set(MutableGame game, MonteCarloHeuristic heuristic, Key key, int value) {
		GameCache<Key, Integer> cache = cache(key);
		if (cache != null)
			cache.set(game, key, value);
		if (sharedCache != null && sharedHeuristics.contains(heuristic.getClass()))
			sharedCache.set(game, heuristic, key, value);
	}

	public void clear(Game game) {
		shortCache.clear(game);
		longCache.clear(game);
	}

	private GameCache<Key, Integer> cache(Key key) {
		return key.level == -1 ? null : key.level == 0 ? shortCache : longCache;
	}

	private class Key {
		private final StateKey state;
		private final Class<?> heuristic;
		private final int level;

		private Key(StateKey state, MonteCarloHeuristic heuristic, int level) {
			this.state = state;
			this.heuristic = heuristic.getClass();
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
			Key other = (Key) obj;
			if (heuristic == null) {
				if (other.heuristic != null)
					return false;
			}
			else if (heuristic != other.heuristic)
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

}
