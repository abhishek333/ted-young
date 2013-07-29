package me.tedyoung.solitaire.tester;

import static me.tedyoung.solitaire.tester.group.Group.allOf;

import java.util.HashSet;

import me.tedyoung.solitaire.framework.AbstractTester;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class DeadlockScanner extends AbstractTester {
	private boolean revised;
	private static final PlayabilityTester PLAYABILITY_TESTER = new PlayabilityTester();

	public DeadlockScanner(PlayerRunControl control) {
		this(control, false);
	}

	public DeadlockScanner(PlayerRunControl control, boolean revised) {
		this.revised = revised;
		setRunControl(control);
	}

	@Override
	public boolean isSolvable(Game g) {
		startGame(g);

		MutableGame game = (MutableGame) g;

		if (!PLAYABILITY_TESTER.isSolvable(game))
			return false;

		CandidateMap candidates = new CandidateMap();

		for (Card card : Card.getAll())
			candidates.put(card, new Candidate(card, game, candidates, revised));

		for (Candidate candidate : candidates.values())
			candidate.initializeDependencies();

		HashSet<Card> dependants = new HashSet<>();
		for (Candidate candidate : candidates.values())
			dependants.addAll(candidate.getDependency().getCards());

		for (Card card : Card.getAll())
			if (!dependants.contains(card))
				candidates.remove(card);

		// game.print(System.out);
		// candidates.dump();
		if (revised)
			return allOf(candidates.getAll(game.getTable().getAllCards())).and(candidates.get(game.getDeck().getAllCards().get(0))).isFreeOfCycle(new SearchContext(getRunControl(), game, revised));
		else
			return allOf(candidates.values()).isFreeOfCycle(new SearchContext(getRunControl(), game, revised));
	}

	@Override
	public String getName() {
		return "DeadlockScanner" + (revised ? "*" : "");
	}
}
