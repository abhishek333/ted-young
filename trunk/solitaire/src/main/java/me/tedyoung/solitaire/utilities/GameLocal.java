package me.tedyoung.solitaire.utilities;

import me.tedyoung.solitaire.game.Game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameLocal<T> {
	private final LoadingCache<Game, T> map;
	private T defaultValue;

	public GameLocal() {
		this(null);
	}

	public GameLocal(final T defaultValue) {
		this.defaultValue = defaultValue;
		this.map = CacheBuilder.newBuilder().initialCapacity(80).weakKeys().concurrencyLevel(10).build(new CacheLoader<Game, T>() {
			@Override
			public T load(Game game) throws Exception {
				return defaultValue();
			}
		});
	}

	public T get(Game game) {
		return map.getUnchecked(game);
	}

	public void set(Game game, T value) {
		map.put(game, value);
	}

	public boolean contains(Game game) {
		return map.getIfPresent(game) != null;
	}

	public void clear(Game game) {
		map.invalidate(game);
	}

	protected T defaultValue() {
		return defaultValue;
	}
}
