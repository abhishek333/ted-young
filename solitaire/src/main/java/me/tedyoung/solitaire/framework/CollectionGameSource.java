package me.tedyoung.solitaire.framework;

import java.util.Collection;
import java.util.Iterator;

import me.tedyoung.solitaire.game.Game;

public class CollectionGameSource extends AbstractGameSource {
	private Iterator<Game> games;

	public CollectionGameSource(Collection<Game> games) {
		super(games.size(), games.iterator().next().getDeck().getHandSize());
		this.games = games.iterator();
	}

	@Override
	protected Game computeNext() {
		if (!games.hasNext())
			return endOfData();
		return games.next();
	}
}
