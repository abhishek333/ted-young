package me.tedyoung.solitaire.tester;

import static me.tedyoung.solitaire.tester.Candidate.canMovementBeRestricted;
import static me.tedyoung.solitaire.tester.Dependency.SUCCEED;
import static me.tedyoung.solitaire.tester.group.Group.anyOf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;

public class DeckManager {
	public DeckManager() {
	}

	public Dependency blockers(Card card, MutableGame game, CandidateMap candidates, boolean revised) {
		List<Card> cards = game.getDeck().getAllCards();

		int index = cards.indexOf(card);
		if (index == cards.size() - 1 || index % 3 == 2)
			return SUCCEED;
		else if (index % 3 == 1)
			return middle(index, cards, candidates, revised);
		else
			return bottom(index, cards, candidates, revised);
	}

	private Dependency bottom(int index, List<Card> cards, CandidateMap candidates, boolean revised) {
		ArrayList<Dependency> options = new ArrayList<>();

		Candidate top = get(index + 2, cards, candidates);
		Candidate middle = get(index + 1, cards, candidates);

		if (revised && canMovementBeRestricted(top.getCard()) && top.getCard().isPeerOf(middle.getCard()) && cards.get(index).isHolderOf(top.getCard()))
			options.add(top.and(middle.restricted()));
		else
			options.add(top.and(middle));

		for (int i = 2; i < index; i += 3)
			options.add(get(i, cards, candidates));

		return anyOf(options);
	}

	private Dependency middle(int index, List<Card> cards, CandidateMap candidates, boolean revised) {
		ArrayList<Dependency> options = new ArrayList<>();

		Candidate candidate = get(index + 1, cards, candidates);
		options.add(candidate);

		for (int i = 2; i < index; i += 3) {
			ArrayList<Candidate> alternatives = new ArrayList<>();
			Candidate top = get(i, cards, candidates);

			alternatives.add(get(i - 1, cards, candidates));

			for (int j = 2; j < i; j += 3)
				alternatives.add(get(j, cards, candidates));

			for (int j = i; j < index; j += 3)
				alternatives.add(get(j + 1, cards, candidates));

			if (revised && canMovementBeRestricted(top.getCard())) {
				for (Iterator<Candidate> iterator = alternatives.iterator(); iterator.hasNext();) {
					candidate = iterator.next();
					if (top.getCard().isPeerOf(candidate.getCard()) && cards.get(index).isHolderOf(top.getCard())) {
						iterator.remove();
						options.add(top.and(candidate.restricted()));
					}

				}
			}

			options.add(top.and(anyOf(alternatives)));
		}

		return anyOf(options);
	}

	private Candidate get(int index, List<Card> cards, CandidateMap candidates) {
		return candidates.get(cards.get(index));
	}

}
