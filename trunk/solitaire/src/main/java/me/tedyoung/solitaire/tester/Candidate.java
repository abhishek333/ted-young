package me.tedyoung.solitaire.tester;

import static me.tedyoung.solitaire.tester.group.Group.allOf;
import static me.tedyoung.solitaire.tester.group.Group.anyOf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;

public class Candidate extends Dependency {
	private static final DeckManager DECK_MANAGER = new DeckManager();

	private final boolean revised;
	private final Card card;
	private final MutableGame game;
	private final CandidateMap candidates;
	private ValueCache cache = new ValueCache();

	private Dependency movementDependency, restrictedMovementDependency;
	private boolean movementRestricted;

	public Candidate(Card card, MutableGame game, CandidateMap candidates, boolean revised) {
		this.card = card;
		this.game = game;
		this.candidates = candidates;
		this.revised = revised;
	}

	public static boolean canMovementBeRestricted(Card card) {
		return card.getDenomination() != Denomination.ACE && card.getDenomination() != Denomination.KING;
	}

	public void initializeDependencies() {
		movementDependency = move(card);

		if (canMovementBeRestricted(card))
			restrictedMovementDependency = restrictedMove(card);
		else
			restrictedMovementDependency = movementDependency;

		if (location(card) == Location.STACK && canMovementBeRestricted(card)) {
			MutableStack stack = game.getTable().getStackContainingCard(card);
			List<Card> cards = stack.getAllCards();
			int index = cards.indexOf(card);
			int peer = cards.indexOf(card.getPeer());
			// Note: cannot be both or top card, either create roughly 1:2000 falshes.
			if (peer != -1 && peer < index) {
				int holder0 = cards.indexOf(card.getHolders().get(0));
				int holder1 = cards.indexOf(card.getHolders().get(1));
				if (Math.max(index, peer) < Math.max(holder0, holder1))
					movementRestricted = true;
			}
		}

	}

	private Dependency move(Card card) {
		switch (location(card)) {
			case STACK:
			case DECK:
				return moveToStack(card).or(moveToFoundation(card));

			case FOUNDATION:
				return moveToStack(card);

			default:
				return FAIL;
		}
	}

	private Dependency restrictedMove(Card card) {
		switch (location(card)) {
			case STACK:
			case DECK:
				return moveToFoundation(card).or(moveToStack(card).and(moveToFoundation(card.getPeer())));

			case FOUNDATION:
				return moveToStack(card).and(moveToFoundation(card.getPeer()));

			default:
				return FAIL;
		}
	}

	private Dependency moveToFoundation(Card card) {
		if (location(card) == Location.FOUNDATION || card.getDenomination() == Denomination.ACE)
			return SUCCEED;
		else
			return reach(card.getPredecessor()).and(moveToFoundation(card.getPredecessor()));
	}

	private Dependency moveToStack(Card card) {
		if (card.getDenomination() == Denomination.KING) {
			if (location(card) == Location.STACK && card == game.getTable().getStackContainingCard(card).getLastCard())
				return SUCCEED;
			else
				return clearAnyStack(card);
		}

		Card holder0 = card.getHolders().get(0);
		Card holder1 = card.getHolders().get(1);
		if (location(holder0) != Location.STACK && location(holder1) != Location.STACK)
			return reach(holder0).or(reach(holder1)).and(moveToStack(holder0));

		ArrayList<Dependency> options = new ArrayList<>();

		for (Card holder : card.getHolders()) {
			switch (location(holder)) {
				case STACK:
					options.add(reach(holder));
					continue;

				case DECK:
				case FOUNDATION:
					options.add(reach(holder).and(moveToStack(holder)));
					continue;

				default:
					return FAIL;
			}
		}

		return anyOf(options);
	}

	private Dependency clearAnyStack(Card card) {
		MutableStack skip = game.getTable().getStackContainingCard(card);

		ArrayList<Dependency> options = new ArrayList<>();

		for (MutableStack stack : game.getTable()) {
			if (stack.isEmpty())
				return SUCCEED;
			else if (stack == skip)
				continue;

			Dependency reach = reach(stack);
			if (reach == SUCCEED)
				return SUCCEED;

			options.add(reach);
		}

		return anyOf(options);
	}

	private Dependency reach(Card card) {
		return blockingCandidates(card, false);
	}

	private Dependency reach(MutableStack stack) {
		return blockingCandidates(stack.getLastCard(), true);
	}

	private Dependency blockingCandidates(Card card, boolean inclusive) {
		List<Card> cards;

		switch (location(card)) {
			case STACK:
				MutableStack stack = game.getTable().getStackContainingCard(card);
				cards = stack.getAllCards();
				ArrayList<Candidate> dependencies = new ArrayList<>(candidates.getAll(cards.subList(0, cards.indexOf(card))));
				if (inclusive && candidates.containsKey(card))
					dependencies.add(candidates.get(card));
				if (dependencies.isEmpty())
					return SUCCEED;
				return allOf(dependencies);

			case DECK:
				return DECK_MANAGER.blockers(card, game, candidates, revised);

			case FOUNDATION:
			default:
				return FAIL;
		}
	}

	private Location location(Card card) {
		if (game.getDeck().containsCard(card))
			return Location.DECK;
		else if (game.getTable().getStackContainingCard(card) != null)
			return Location.STACK;
		else
			return Location.FOUNDATION;
	}

	@Override
	public boolean isFreeOfCycle(SearchContext context) {
		if (context.isCached(card))
			return context.getCachedValue(card);

		if (!context.start(card))
			return false;

		if (cache.isCached(movementRestricted))
			return context.end(cache.getValue(movementRestricted));

		boolean value = getDependency().isFreeOfCycle(context);

		if (value)
			cache.setValue(movementRestricted, value);

		return context.end(value);
	}

	public Dependency restricted() {
		return new Dependency() {
			@Override
			public boolean isConstant() {
				return false;
			}

			@Override
			public boolean isFreeOfCycle(SearchContext context) {
				Candidate.this.movementRestricted = true;
				boolean result = Candidate.this.isFreeOfCycle(context);
				Candidate.this.movementRestricted = false;
				return result;
			}

			@Override
			public Set<Card> getCards() {
				return Candidate.this.getCards();
			}

			@Override
			public String toString() {
				return Candidate.this.toString() + "*";
			}
		};
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public String toString() {
		return card.toString() + (movementRestricted ? "*" : "");
	}

	public String dump() {
		return toString() + " " + getDependency();
	}

	@Override
	public Set<Card> getCards() {
		return EnumSet.of(card);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
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
		Candidate other = (Candidate) obj;
		if (card == null) {
			if (other.card != null)
				return false;
		}
		else if (!card.equals(other.card))
			return false;
		return true;
	}

	public Card getCard() {
		return card;
	}

	public Dependency getDependency() {
		return movementRestricted ? restrictedMovementDependency : movementDependency;
	}
}
