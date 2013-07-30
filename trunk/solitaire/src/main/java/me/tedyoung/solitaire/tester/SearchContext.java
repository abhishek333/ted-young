package me.tedyoung.solitaire.tester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class SearchContext {
	private final PlayerRunControl control;
	private final MutableGame game;

	private boolean revised;
	private LinkedList<Card> stack = new LinkedList<>();
	private LinkedList<Set<Card>> localCache = new LinkedList<>();
	private Map<CacheNode, Boolean> globalCache = new HashMap<>();
	private Card cycle;

	public SearchContext(PlayerRunControl control, MutableGame game, boolean revised) {
		this.revised = revised;
		this.control = control;
		this.game = game;
		start(null);
	}

	public boolean start(Card card) {
		int index = stack.indexOf(card);
		if (index > -1) {
			if (cycle == null || stack.indexOf(cycle) < index)
				cycle = card;
			return false;
		}
		else {
			stack.addFirst(card);
			localCache.addFirst(new HashSet<Card>());
			control.verify(game);
			return true;
		}
	}

	public boolean end(boolean value) {
		Card card = stack.removeFirst();
		localCache.removeFirst();

		if (stack.getFirst() == cycle) {
			cycle = null;
			if (!value)
				globalCache.put(node(card), false);
		}

		return value;
	}

	public boolean isCached(Card card) {
		return localCache.getFirst().contains(card) || globalCache.containsKey(node(card));
	}

	public boolean getCachedValue(Card card) {
		return localCache.getFirst().contains(card) ? false : globalCache.get(node(card));
	}

	public boolean isRevised() {
		return revised;
	}

	private CacheNode node(Card card) {
		return new CacheNode(stack.getFirst(), card, false);
	}

	private static class CacheNode {
		private final Card from, to;
		private final boolean restricted;

		public CacheNode(Card from, Card to, boolean peerGroup) {
			this.from = from;
			this.to = to;
			this.restricted = peerGroup;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + (restricted ? 1231 : 1237);
			result = prime * result + ((to == null) ? 0 : to.hashCode());
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
			CacheNode other = (CacheNode) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			}
			else if (!from.equals(other.from))
				return false;
			if (restricted != other.restricted)
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			}
			else if (!to.equals(other.to))
				return false;
			return true;
		}

	}
}
