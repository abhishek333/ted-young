package me.tedyoung.solitaire.utilities;

import me.tedyoung.solitaire.game.Game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameCache<K, V> {
	private GameLocal<LoadingCache<K, V>> map;
	private V defaultValue;

	public GameCache() {
		this(Integer.MAX_VALUE);
	}

	public GameCache(int maximumCapacity) {
		this(maximumCapacity, null);
	}

	public GameCache(final int maximumCapacity, final V defaultValue) {
		this.defaultValue = defaultValue;
		this.map = new GameLocal<LoadingCache<K, V>>() {
			@Override
			protected LoadingCache<K, V> defaultValue() {
				return CacheBuilder.newBuilder().initialCapacity(10_000).maximumSize(maximumCapacity).concurrencyLevel(1).build(new CacheLoader<K, V>() {
					@Override
					public V load(K key) throws Exception {
						return GameCache.this.defaultValue();
					}
				});
			}
		};
	}

	public LoadingCache<K, V> get(Game game) {
		return map.get(game);
	}

	public V get(Game game, K key) {
		return map.get(game).getUnchecked(key);
	}

	public V getIfPresent(Game game, K key) {
		return map.get(game).getIfPresent(key);
	}

	public void set(Game game, K key, V value) {
		map.get(game).put(key, value);
	}

	public void share(Game source, Game destination) {
		map.set(destination, map.get(source));
	}

	public void clear(Game game, K key) {
		map.get(game).invalidate(key);
	}

	public void clear(Game game) {
		map.clear(game);
	}

	protected V defaultValue() {
		return defaultValue;
	}
}
