package me.tedyoung.solitaire.tester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.utilities.CardMap;

public class CandidateMap extends CardMap<Candidate> {
	public CandidateMap() {
	}

	public Collection<Candidate> getAll(Collection<Card> cards) {
		ArrayList<Candidate> candidates = new ArrayList<>();
		for (Card card : cards)
			candidates.add(get(card));
		return candidates;
	}

	public void dump() {
		for (Map.Entry<Card, Candidate> entry : entrySet())
			System.out.println(entry.getKey() + "=" + entry.getValue().dump());
	}
}
