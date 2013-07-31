package me.tedyoung.solitaire.framework.heuristic;

import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.Stack;
import me.tedyoung.solitaire.game.move.DeckToFoundationMove;
import me.tedyoung.solitaire.game.move.DeckToStackMove;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToFoundationMove;
import me.tedyoung.solitaire.game.move.StackToStackMove;

public class AdvancedMoveHeuristic implements MoveHeuristic {
	private boolean solver;

	public AdvancedMoveHeuristic() {
		this(false);
	}

	public AdvancedMoveHeuristic(boolean solver) {
		this.solver = solver;
	}

	@Override
	public int valueOf(Move move, Game game) {
		Card card = move.getCard();

		int bias = 0;

		if (solver) {
			for (MutableStack stack : ((MutableGame) game).getTable()) {
				List<Card> hidden = stack.getHiddenCards();
				if (hidden.isEmpty())
					continue;
				List<Card> cards = stack.getAllCards();
				for (Card control : cards.subList(0, cards.size() - 1))
					bias += deadlockAvoidanceBias(control, hidden);
			}

			if (move instanceof StackToStackMove || move instanceof DeckToStackMove || move instanceof FoundationToStackMove) {
				int index = 0;

				if (move instanceof StackToStackMove)
					index = ((StackToStackMove) move).getDestination();
				else if (move instanceof DeckToStackMove)
					index = ((DeckToStackMove) move).getStack();
				else if (move instanceof FoundationToStackMove)
					index = ((FoundationToStackMove) move).getStack();

				MutableStack stack = ((MutableGame) game).getStack(index);
				if (stack.getHiddenCards().size() > 0)
					bias += deadlockAvoidanceBias(card, stack.getHiddenCards());
			}
		}

		if (move instanceof StackToFoundationMove || move instanceof DeckToFoundationMove) {
			// Don't let anyone foundation get too far ahead of the others
			if (cardShouldBePlayedFoundation(game, card))
				return 200 + bias;

			// Prefer a deck to stack move if doing so will uncover a card
			if (move instanceof DeckToStackMove && canBePlayedToStack(game, card) && willMovingStackToThisCardExposeACard(game, card))
				return 210 + bias;

			// Otherwise played to the foundation
			return 500 + bias;
		}

		if (move instanceof StackToStackMove) {
			Stack source = game.getStack(((StackToStackMove) move).getSource());

			if (card.equals(source.getLastVisibleCard())) {
				// Expose a card
				if (source.getNumberOfHiddenCards() != 0)
					return 450 + bias;

				// Expose a needed blank spot
				if (source.getNumberOfHiddenCards() == 0 && card.getDenomination() != Denomination.KING && numberOfBlankSpacesNeeded(game) > 0)
					return 420 + bias;
			}
			else {
				// Uncover a card that can be played to the foundation
				if (game.getFoundation().isPlayable(source.getVisibleCards().get(source.getVisibleCards().indexOf(card) + 1)))
					return 220 + bias;
			}

			// Otherwise avoid useless moves
			return 0;
		}

		if (move instanceof DeckToStackMove) {
			if (game.getDeck().getHandSize() == 1) {
				Stack stack = game.getStack(((DeckToStackMove) move).getStack());
				if (stack.getVisibleCards().size() > 1 && move.getCard().getSuit() == stack.getVisibleCards().get(1).getSuit())
					bias += 5;
			}

			// Uncover a card that can be played to the foundation (does not make sense in a 1 hand game, but does not hurt score)
			if (game.getDeck().getVisibleCards().size() > 1 && game.getFoundation().isPlayable(game.getDeck().getVisibleCards().get(1)))
				return 350 + bias;

			// The card can be used as a landing spot for another stack, exposing the card in the process
			if (willMovingStackToThisCardExposeACard(game, card))
				return 320 + bias;

			// Otherwise avoid unnecessary use of the deck
			return 100 + bias;
		}

		if (move instanceof FoundationToStackMove) {
			// The move is only useful if it will serve as a landing spot for another stack, exposing a card in the process
			if (willMovingStackToThisCardExposeACard(game, card))
				return 250 + bias;

			// Otherwise avoid this move as much as possible
			return -100 + bias;
		}

		return 0 + bias;
	}

	private int deadlockAvoidanceBias(Card card, List<Card> cards) {
		int bias = 0;

		for (Card blocked : cards.subList(cards.indexOf(card) + 1, cards.size())) {
			if (blocked.isPeerOf(card))
				bias += -5;

			if (blocked.isPredecessorOf(card))
				bias += -5;

			if (blocked.isHolderOf(card))
				bias += -10;
		}
		return bias;
	}

	private boolean cardShouldBePlayedFoundation(Game game, Card card) {
		if (card.getDenomination().compareTo(Denomination.TWO) > 0)
			for (Card foundation : game.getFoundation().getTopCards())
				if (foundation == null || card.getDenomination().getValue() - foundation.getDenomination().getValue() > 3)
					return false;
		return true;
	}

	private int numberOfBlankSpacesNeeded(Game game) {
		int count = 4;
		for (Stack stack : game.getTable())
			if (stack.isEmpty() || (stack.getLastVisibleCard().getDenomination() == Denomination.KING && stack.getNumberOfHiddenCards() == 0))
				count--;
		return count;
	}

	private boolean canBePlayedToStack(Game game, Card card) {
		for (Stack stack : game.getTable())
			if (stack.isPlayable(card))
				return true;
		return false;
	}

	private boolean willMovingStackToThisCardExposeACard(Game game, Card card) {
		for (Stack stack : game.getTable()) {
			if (stack.getNumberOfHiddenCards() == 0)
				continue;

			Card cardFromStack = stack.getLastVisibleCard();
			if (cardFromStack == null)
				continue;

			if (cardFromStack.getSuit().getColor() != card.getSuit().getColor() && cardFromStack.getDenomination().isOneLowerThan(card.getDenomination()))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Advance Move Heuristic";
	}

}
