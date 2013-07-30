package me.tedyoung.solitaire.tester;

import me.tedyoung.solitaire.framework.AbstractTester;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.tester.group.Group;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class DeadlockTester extends AbstractTester {
	private boolean revised;
	private static final PlayabilityTester PLAYABILITY_TESTER = new PlayabilityTester();

	public DeadlockTester() {
		this(new PlayerRunControl(), false);
	}

	public DeadlockTester(PlayerRunControl control) {
		this(control, false);
	}

	public DeadlockTester(PlayerRunControl control, boolean revised) {
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

		// if (revised) {
		// Set<Card> dependants = EnumSet.noneOf(Card.class);
		// for (Candidate candidate : candidates.values())
		// dependants.addAll(candidate.getDependency().getCards());
		//
		// return Group.allOf(candidates.getAll(dependants)).isFreeOfCycle(new SearchContext(getRunControl(), game, revised));
		// }

		return Group.allOf(candidates.values()).isFreeOfCycle(new SearchContext(getRunControl(), game, revised));
	}

	@Override
	public String getName() {
		return "DeadlockScanner" + (revised ? "*" : "");
	}
}
