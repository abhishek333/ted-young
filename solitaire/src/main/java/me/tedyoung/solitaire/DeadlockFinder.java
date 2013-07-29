package me.tedyoung.solitaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tedyoung.solitaire.framework.Tester;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

@Deprecated
public class DeadlockFinder implements Tester {
	@Override
	public boolean isSolvable(Game g) {
		MutableGame game = (MutableGame) g;
		// game.print(System.out);

		Set<Candidate> candidates = new HashSet<>();

		for (Card card : Card.getAll()) {
			Candidate candidate = Candidate.forCard(card, game);
			if (candidate != null)
				candidates.add(candidate);
		}

		if (candidates.isEmpty())
			return true;

		for (Candidate candidate : candidates)
			candidate.findBlockers(candidates);

		// System.out.println(candidates);

		return Group.allOf(candidates).isFreeOfCycle();
	}

	private static Candidate find(Collection<Candidate> candidates, Card card) {
		for (Candidate candidate : candidates)
			if (candidate.card == card)
				return candidate;
		return null;
	}

	private static class Candidate implements Dependency {
		private Card card;
		private final MutableGame game;
		private final ArrayList<Dependency> blockers = new ArrayList<>();
		private boolean possiblyInCycle;

		private Candidate(Card card, MutableGame game) {
			this.card = card;
			this.game = game;
		}

		private static boolean cannotBeBlocked(Card card, MutableGame game) {
			MutableStack stack = game.getTable().getStackContainingCard(card);
			if (stack != null) {
				return stack.getTopCard() == card;
			}
			else {
				List<Card> cards = game.getDeck().getAllCards();
				int index = cards.indexOf(card);
				return index == cards.size() - 1 || index % 3 == 2;
			}
		}

		private static boolean cannotBlock(Card card, MutableGame game) {
			if (card.getDenomination() == Denomination.ACE)
				return true;

			MutableStack stack = game.getTable().getStackContainingCard(card);
			if (stack != null)
				return card.getDenomination() == Denomination.KING && stack.getLastCard() == card; // Kings
			else
				return game.getDeck().getAllCards().indexOf(card) == 0;
		}

		public static Candidate forCard(Card card, MutableGame game) {
			if (cannotBlock(card, game))
				return null;

			Candidate candidate = new Candidate(card, game);
			for (Card holder : card.getHolders())
				if (cannotBeBlocked(holder, game))
					return null;

			for (Card predecessor : card.getPredecessors())
				if (!cannotBeBlocked(predecessor, game))
					return candidate;

			return null;
		}

		public void findBlockers(Set<Candidate> candidates) {
			if (card.getDenomination() == Denomination.KING) {
				MutableStack stack = game.getTable().getStackContainingCard(card);
				if (stack != null && stack.getLastCard() == card)
					return;
				Group group = findBlockersForKing(candidates);
				if (group == null)
					return;
				blockers.add(group);
			}
			else {
				Group group = findHolderBlockers(card, candidates);
				if (group == null)
					return;
				blockers.add(group);
			}

			Group predecessorBlockers = findPredecessorBlockers(card, candidates);
			if (predecessorBlockers == null)
				return;
			blockers.add(predecessorBlockers);

			possiblyInCycle = true;
		}

		private Group findPredecessorBlockers(Card card, Set<Candidate> candidates) {
			ArrayList<Dependency> predecessorBlockers = new ArrayList<>();
			for (Card predecessor : card.getPredecessors()) {
				Group group = findBlockers(predecessor, candidates);
				if (group != null)
					predecessorBlockers.add(group);
			}

			if (predecessorBlockers.isEmpty())
				return null;
			return Group.allOf(predecessorBlockers);
		}

		private Group findHolderBlockers(Card card, Set<Candidate> candidates) {
			if (card.getDenomination() == Denomination.KING)
				return findBlockersForKing(candidates);

			Group holderGroup = findHolderBlockers(card.getHolders().get(0), candidates);

			ArrayList<Dependency> blockers = new ArrayList<Dependency>();
			for (Card holder : card.getHolders()) {
				Group group = Group.allOf(findBlockers(holder, candidates), holderGroup);
				if (group.isEmpty())
					return null;
				blockers.add(group);
			}
			return Group.anyOf(blockers);
		}

		private Group findBlockers(Card dependency, Set<Candidate> candidates) {
			MutableStack stack = game.getTable().getStackContainingCard(dependency);
			if (stack != null) {
				List<Card> cards = stack.getAllCards();
				ArrayList<Candidate> blockers = new ArrayList<>();
				int index = cards.indexOf(dependency);

				for (Candidate candidate : candidates)
					if (cards.contains(candidate.card) && cards.indexOf(candidate.card) < index)
						blockers.add(candidate);

				if (blockers.isEmpty())
					return null;

				return Group.allOf(blockers);
			}
			else {
				List<Card> cards = game.getDeck().getAllCards();
				int index = cards.indexOf(dependency);
				int position = index % 3;
				int top = Math.min(index + 2 - position, cards.size() - 1);

				if (position == top)
					return null;

				Candidate topCandidate = find(candidates, cards.get(top));
				if (position == top - 2) {
					Candidate middleCandidate = find(candidates, cards.get(top - 1));
					Group upperGroup = Group.allOf(topCandidate, middleCandidate);
					if (upperGroup.isEmpty())
						return null;

					ArrayList<Dependency> groups = new ArrayList<>();
					for (int i = 2; i < top; i += 3) {
						Candidate candidate = find(candidates, cards.get(i));
						if (candidate == null)
							return null;
						groups.add(candidate);
					}

					groups.add(upperGroup);
					return Group.anyOf(groups);
				}
				else {
					if (topCandidate == null)
						return null;

					ArrayList<Dependency> groups = new ArrayList<>();
					for (int i = 2; i < top; i += 3) {
						Candidate icandidate = find(candidates, cards.get(i));
						for (int j = 2; j < i; j += 3) {
							Candidate jcandidate = find(candidates, cards.get(j));
							if (icandidate == null && jcandidate == null)
								return null;
							groups.add(Group.allOf(icandidate, jcandidate));
						}
					}

					groups.add(topCandidate);
					return Group.anyOf(groups);
				}
			}
		}

		protected Group findBlockersForKing(Set<Candidate> candidates) {
			ArrayList<ArrayList<Dependency>> stacks = new ArrayList<>();
			for (int i = 0; i < 7; i++)
				stacks.add(new ArrayList<Dependency>());

			for (Candidate candidate : candidates) {
				MutableStack stack = game.getTable().getStackContainingCard(candidate.card);
				if (stack != null)
					stacks.get(stack.getIndex()).add(candidate);
			}

			ArrayList<Dependency> blockers = new ArrayList<Dependency>();
			for (List<Dependency> stack : stacks)
				if (stack.isEmpty())
					return null;
				else
					blockers.add(Group.allOf(stack));

			return Group.anyOf(blockers);
		}

		private boolean inScan;

		@Override
		public boolean isFreeOfCycle() {
			if (!possiblyInCycle)
				return true;

			if (inScan)
				return false;

			try {
				inScan = true;
				return Group.anyOf(blockers).isFreeOfCycle();
			}
			finally {
				inScan = false;
			}
		}

	}

	public static class Group implements Dependency {
		private final boolean all;
		private ArrayList<Dependency> dependencies = new ArrayList<>();

		public Group(boolean all) {
			this.all = all;
		}

		public static Group anyOf(Dependency... dependencies) {
			return anyOf(Arrays.asList(dependencies));
		}

		public static Group anyOf(Collection<? extends Dependency> dependencies) {
			Group group = new Group(false);
			group.add(dependencies);
			return group;
		}

		public static Group allOf(Dependency... dependencies) {
			return allOf(Arrays.asList(dependencies));
		}

		public static Group allOf(Collection<? extends Dependency> dependencies) {
			Group group = new Group(true);
			group.add(dependencies);
			return group;
		}

		private void add(Collection<? extends Dependency> dependencies) {
			checkSize(dependencies);
			for (Dependency dependency : dependencies)
				if (dependency != null)
					this.dependencies.add(dependency);
		}

		@Override
		public boolean isFreeOfCycle() {
			if (isEmpty())
				throw new IllegalArgumentException();

			if (all) {
				for (Dependency dependency : dependencies)
					if (!dependency.isFreeOfCycle())
						return false;
				return true;
			}
			else {
				for (Dependency dependency : dependencies)
					if (dependency.isFreeOfCycle())
						return true;
				return false;
			}
		}

		public boolean isEmpty() {
			for (Dependency dependency : dependencies)
				if (!(dependency instanceof Group && ((Group) dependency).isEmpty()))
					return false;
			return true;
		}

		private static void checkSize(Collection<? extends Dependency> dependencies) {
			if (dependencies.isEmpty())
				throw new IllegalArgumentException("Cannot create empty group.");
		}

	}

	private static interface Dependency {
		boolean isFreeOfCycle();
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void pause(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRunControl(PlayerRunControl control) {
		// TODO Auto-generated method stub

	}
}
