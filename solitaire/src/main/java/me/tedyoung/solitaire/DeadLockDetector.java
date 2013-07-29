package me.tedyoung.solitaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tedyoung.solitaire.framework.Tester;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.tester.Location;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

@Deprecated
public class DeadLockDetector implements Tester {
	private MutableGame game;
	private Map<Card, CardNode> cards = new HashMap<>();
	private List<StackNode> stacks = new ArrayList<>();

	@Override
	public boolean isSolvable(Game game) {
		this.game = (MutableGame) game;

		for (MutableStack stack : game.getTable()) {
			CardNode previous = null;
			for (Card card : stack.getAllCards()) {
				CardNode node = new CardNode(card, previous);
				cards.put(card, node);
				previous = node;
			}
			stacks.add(new StackNode(previous));
		}

		ArrayList<CardNode> tops = new ArrayList<>();
		ArrayList<BlockerNode> middles = new ArrayList<>();
		for (List<Card> hand : this.game.getDeck().getHands()) {
			Card card = hand.get(0);
			CardNode top = new CardNode(card);
			cards.put(card, top);

			CardNode middle = null;
			if (hand.size() > 2) {
				card = hand.get(1);
				middle = new CardNode(card, top);
				middle.getBlockers().addAll(middles);
				cards.put(card, middle);
			}

			CardNode bottom = null;
			if (hand.size() > 1) {
				card = hand.get(hand.size() - 1);
				bottom = new CardNode(card, middle == null ? top : middle);
				bottom.getBlockers().addAll(tops);
				cards.put(card, bottom);
			}

			if (hand.size() > 2) {
				middles.add(new BlockerNode(top, middle));
				for (CardNode node : tops) {
					middles.add(new BlockerNode(node, bottom));
					middle.getBlockers().add(new BlockerNode(node, bottom));
				}
			}

			for (CardNode node : tops)
				middles.add(new BlockerNode(node, top)); // first, second

			tops.add(top);
		}

		// Does not improve success rate.
		// if (!cards.get(game.getDeck().getAllCards().get(0)).isMovable())
		// return false;

		for (int index = 6; index >= 0; index--)
			if (!stacks.get(index).isClearable())
				return false;

		return true;
	}

	private interface Node {
		boolean isMovable();
	}

	private class CardNode implements Node {
		private final Card card;
		private final List<Node> blockers = new ArrayList<>();

		protected Boolean isMovable;
		protected Boolean isReachable;

		public CardNode(Card card, Node... blockers) {
			this(card, Arrays.asList(blockers));
		}

		public CardNode(Card card, Collection<Node> blockers) {
			this.card = card;
			for (Node node : blockers)
				if (node != null)
					this.blockers.add(node);
		}

		private Location getLocation() {
			if (game.getDeck().containsCard(card))
				return Location.DECK;
			else if (game.getTable().getStackContainingCard(card) != null)
				return Location.STACK;
			else
				return Location.FOUNDATION;
		}

		@Override
		public boolean isMovable() {
			if (isMovable != null)
				return isMovable;

			isMovable = false;
			if (isMovableImpl()) {
				isMovable = true;
				return true;
			}
			else {
				isMovable = null;
				return false;
			}
		}

		protected boolean isMovableImpl() {
			boolean isVisible = getLocation() == Location.STACK && game.getTable().getStackContainingCard(card).getVisibleCards().contains(card);
			if (!isVisible && !isReachable())
				return false;

			switch (getLocation()) {
				case DECK:
					return isPlayableAnywhere();

				case STACK:
					return isPlayableAnywhere();

				case FOUNDATION:
					return isPlayableToStack();
			}

			return false;
		}

		private boolean isPlayableAnywhere() {
			if (card.getDenomination().getValue() > 6)
				return isPlayableToStack() || isPlayableToFoundation();
			else
				return isPlayableToFoundation() || isPlayableToStack();
		}

		private boolean isPlayableToFoundation() {
			if (getLocation() == Location.FOUNDATION)
				return true;

			if (card.getDenomination() == Denomination.ACE)
				return true;

			List<CardNode> nodes = cardNodes(card.getPredecessors(), REACHABILITY, false);
			if (nodes.get(0).isReachable != null && !nodes.get(0).isReachable)
				return false;

			CardNode predecessor = cards.get(card.getPredecessor());
			return predecessor.isReachable() && predecessor.isPlayableToFoundation();
		}

		private boolean isPlayableToStack() {
			if (card.getDenomination() == Denomination.KING) {
				if (getLocation() == Location.STACK && card == game.getTable().getStackContainingCard(card).getLastCard())
					return true;

				List<StackNode> nodes = stackNodes(game.getTable().getStacks(), CLEARABILITY, true);
				if (nodes.get(0).isClearable != null && nodes.get(0).isClearable)
					return true;

				for (StackNode node : nodes)
					if (node.isClearable())
						return true;
				return false;
			}
			else {
				if (getLocation() == Location.STACK) {
					List<Card> stack = game.getTable().getStackContainingCard(card).getAllCards();
					if (stack.size() >= 3) {
						int index = stack.indexOf(card);
						int peer = stack.indexOf(card.getPeer());
						if (-1 < peer && peer < index && index < Math.max(stack.indexOf(card.getHolders().get(0)), stack.indexOf(card.getHolders().get(1))))
							return cards.get(card.getPeer()).isPlayableToFoundation();
					}
				}
				else if (getLocation() == Location.DECK) {
					List<Card> deck = game.getDeck().getAllCards();
					int index = deck.indexOf(card);
					int peer = deck.indexOf(card.getPeer());
					if (peer % 3 == 2 && index == peer - 1 && Math.max(deck.indexOf(card.getHolders().get(0)), deck.indexOf(card.getHolders().get(1))) == index - 2)
						return cards.get(card.getPeer()).isPlayableToFoundation();
				}

				for (CardNode holder : cardNodes(card.getHolders(), REACHABILITY, true))
					if (holder.isReachable() && holder.canBeFoundOnStack())
						return true;
				return false;
			}
		}

		private boolean canBeFoundOnStack() {
			switch (getLocation()) {
				case DECK:
					return isPlayableToStack();

				case STACK:
					return true;

				case FOUNDATION:
					return isPlayableToStack();
			}

			return false;
		}

		protected boolean isReachable() {
			if (isReachable != null)
				return isReachable;
			isReachable = false;
			if (isReachableImpl()) {
				isReachable = true;
				return true;
			}
			else {
				isReachable = null;
				return false;
			}
		}

		protected boolean isReachableImpl() {
			if (blockers.isEmpty())
				return true;

			if (getLocation() == Location.STACK) {
				CardNode node = this;
				while (!node.getBlockers().isEmpty()) {
					CardNode blocker = (CardNode) node.getBlockers().get(0);
					if (blocker.isMovable != null && !blocker.isMovable)
						return false;
					node = blocker;
				}
			}

			Map<Card, Boolean> cache = new HashMap<>();
			for (Node blocker : blockers) {
				if (blocker instanceof BlockerNode) {
					if (((BlockerNode) blocker).isMovable(cache, this))
						return true;
				}
				else {
					if (blocker.isMovable())
						return true;
				}
			}

			return false;
		}

		public List<Node> getBlockers() {
			return blockers;
		}
	}

	private class BlockerNode implements Node {
		private List<CardNode> cards = new ArrayList<>();

		public BlockerNode(CardNode... cards) {
			this.cards.addAll(Arrays.asList(cards));
		}

		@Override
		public boolean isMovable() {
			return isMovable(null, null);
		}

		public boolean isMovable(Map<Card, Boolean> cache, CardNode previous) {
			for (CardNode card : cards)
				if (card.isMovable != null)
					return card.isMovable;

			boolean peerMode = previous.card.isHolderOf(cards.get(0).card) && cards.get(0).card.isPeerOf(cards.get(1).card);

			if (!peerMode)
				for (CardNode card : cards)
					if (cache != null && cache.containsKey(card))
						return cache.get(card);

			for (CardNode card : cards) {
				ArrayList<Node> blockers = new ArrayList<>(card.getBlockers());
				try {
					card.getBlockers().clear();
					if (!card.isMovable()) {
						if (cache != null)
							cache.put(card.card, false);
						return false;
					}
					if (cache != null)
						cache.put(card.card, true);

					if (peerMode) {
						CardNode card1 = cards.get(1);
						ArrayList<Node> blockers1 = new ArrayList<>(card1.getBlockers());
						try {
							card1.getBlockers().clear();
							if (!(card1.isReachable() && (card1.isPlayableToFoundation() || card.isPlayableToFoundation())))
								return false;
						}
						finally {
							card1.getBlockers().addAll(blockers1);
						}
						break;
					}
				}
				finally {
					card.getBlockers().addAll(blockers);
				}
			}

			return true;
		}
	}

	private class StackNode implements Node {
		private final CardNode blocker;

		protected Boolean isClearable;

		public StackNode(CardNode blocker) {
			this.blocker = blocker;
		}

		@Override
		public boolean isMovable() {
			return isClearable();
		}

		public boolean isClearable() {
			if (isClearable != null)
				return isClearable;

			isClearable = false;
			if (isClearableImpl()) {
				isClearable = true;
				return true;
			}
			else {
				isClearable = null;
				return false;
			}
		}

		private boolean isClearableImpl() {
			if (blocker == null)
				return true;
			else
				return blocker.isMovable();
		}
	}

	private List<CardNode> cardNodes(Collection<Card> list, Comparator<CardNode> order, boolean truesFirst) {
		ArrayList<CardNode> nodes = new ArrayList<>(list.size());
		for (Card card : list)
			nodes.add(cards.get(card));

		if (!truesFirst)
			order = Collections.reverseOrder(order);
		Collections.sort(nodes, order);

		return nodes;
	}

	private List<StackNode> stackNodes(Collection<MutableStack> list, Comparator<StackNode> order, boolean truesFirst) {
		ArrayList<StackNode> nodes = new ArrayList<>(list.size());
		for (MutableStack stack : list)
			nodes.add(stacks.get(stack.getIndex()));

		if (!truesFirst)
			order = Collections.reverseOrder(order);
		Collections.sort(nodes, order);

		return nodes;
	}

	private static final Comparator<CardNode> REACHABILITY = new Comparator<CardNode>() {
		@Override
		public int compare(CardNode a, CardNode b) {
			return Integer.compare(a.isReachable == null ? 0 : a.isReachable ? -1 : 1, b.isReachable == null ? 0 : b.isReachable ? -1 : 1);
		}
	};

	private static final Comparator<StackNode> CLEARABILITY = new Comparator<StackNode>() {
		@Override
		public int compare(StackNode a, StackNode b) {
			return Integer.compare(a.isClearable == null ? 0 : a.isClearable ? -1 : 1, b.isClearable == null ? 0 : b.isClearable ? -1 : 1);
		}
	};

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
