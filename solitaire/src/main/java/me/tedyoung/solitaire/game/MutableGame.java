package me.tedyoung.solitaire.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import me.tedyoung.solitaire.game.move.DeckToFoundationMove;
import me.tedyoung.solitaire.game.move.DeckToStackMove;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToFoundationMove;
import me.tedyoung.solitaire.game.move.StackToStackMove;

public class MutableGame implements Game, Serializable {
	private static final long serialVersionUID = 1L;

	private MutableDeck deck;
	private MutableFoundation foundation;
	private MutableTable table;

	private transient History history = new History();

	public MutableGame() {
		this(3);
		history = new History();
	}

	public MutableGame(int handSize) {
		this(handSize, new Random());
	}

	public MutableGame(int handSize, Random random) {
		deck = new MutableDeck(handSize, random);
		foundation = new MutableFoundation();
		table = new MutableTable(deck);
	}

	MutableGame(MutableGame game, boolean includeHistory) {
		deck = game.deck.clone();
		foundation = game.foundation.clone();
		table = game.table.clone();

		if (includeHistory)
			history = new History(game.history);
	}

	public void play(Move move) {
		history.add(getSavedState(), move);
		move.apply(this);
	}

	public SavedState getSavedState() {
		return new SavedState(this);
	}

	public void undo() {
		restoreState(history.remove());
	}

	public void reset() {
		restoreState(history.clear());
	}

	public int mark() {
		return history.mark();
	}

	public void restore(int mark) {
		restoreState(history.restore(mark));
	}

	private void restoreState(SavedState state) {
		if (state == null)
			return;

		deck = state.getDeck();
		foundation = state.getFoundation();
		table = state.getTable();
	}

	@Override
	public boolean isInCycle() {
		return history.contains(SavedState.proxy(this));
	}

	private List<Move> getMovesFromDeck(Card card, int deals, boolean flip) {
		ArrayList<Move> moves = new ArrayList<Move>();

		if (foundation.isPlayable(card))
			moves.add(new DeckToFoundationMove(card, deals, flip));

		for (Stack stack : table.getPlayableStacks(card))
			moves.add(new DeckToStackMove(card, stack, deals, flip));

		return moves;
	}

	public List<Move> getAllLegalMoves() {
		LinkedList<Move> moves = new LinkedList<>();

		if (!deck.isEmpty()) {
			HashSet<Card> cards = new HashSet<Card>();

			// TODO: Should this be moved?
			if (deck.getVisibleCards().isEmpty())
				deck.dealNextHand();

			int deal = 0;
			for (Card card : deck.getThisAndSubsequentVisibleCards()) {
				if (!cards.contains(card)) {
					cards.add(card);
					moves.addAll(getMovesFromDeck(card, deal++, false));
				}
			}

			deal = 0;
			for (Card card : deck.getSubsequentVisibleCardsAfterFlip()) {
				if (!cards.contains(card)) {
					cards.add(card);
					moves.addAll(getMovesFromDeck(card, deal++, true));
				}
			}
		}

		Stack firstEmptyStack = table.getFirstEmptyStack();

		for (MutableStack source : table) {
			if (source.isEmpty())
				continue;

			Card sourceCard = source.getTopCard();

			if (foundation.isPlayable(sourceCard))
				moves.add(new StackToFoundationMove(sourceCard, source));

			for (Card card : source.getVisibleCards()) {
				if (card.getDenomination() == Denomination.KING) {
					if (source.getHiddenCards().size() != 0 && firstEmptyStack != null)
						moves.add(new StackToStackMove(card, source, firstEmptyStack));
				}
				else {
					for (Stack destination : table.getPlayableStacks(card))
						if (source.getIndex() != destination.getIndex())
							moves.add(new StackToStackMove(card, source, destination));
				}
			}
		}

		for (Card card : foundation.getTopCards()) {
			if (card.getDenomination() == Denomination.KING) {
				if (firstEmptyStack != null)
					moves.add(new FoundationToStackMove(card, firstEmptyStack));
			}
			else {
				for (Stack stack : table.getPlayableStacks(card))
					moves.add(new FoundationToStackMove(card, stack));
			}
		}

		for (ListIterator<Move> iterator = moves.listIterator(); iterator.hasNext();) {
			Move move = iterator.next();
			if (move instanceof FoundationToStackMove || move instanceof StackToFoundationMove || move instanceof StackToStackMove) {
				if (move instanceof StackToStackMove)
					if (move.getCard().equals(table.getStack(((StackToStackMove) move).getSource()).getLastVisibleCard()))
						continue;

				play(move);
				if (isInCycle())
					iterator.remove();
				undo();
			}
		}

		return moves;
	}

	@Override
	public MutableDeck getDeck() {
		return deck;
	}

	@Override
	public MutableFoundation getFoundation() {
		return foundation;
	}

	@Override
	public MutableTable getTable() {
		return table;
	}

	@Override
	public MutableStack getStack(int index) {
		return table.getStack(index);
	}

	@Override
	public boolean isComplete() {
		return foundation.isComplete();
	}

	@Override
	public int getNumberOfCards() {
		int count = 0;
		count += foundation.size();
		count += table.size();
		count += deck.size();
		return count;
	}

	public int getNumberOfMovesPlayed() {
		return history.size();
	}

	public History getHistory() {
		return history;
	}

	@Override
	public MutableGame createPlayerCopy() {
		return createPlayerCopy(true);
	}

	public MutableGame createPlayerCopy(boolean redactDeck) {
		MutableGame clone = new MutableGame(this, false);
		if (redactDeck)
			clone.deck.redact();
		for (MutableStack stack : clone.table)
			stack.redact();
		return clone;
	}

	@Override
	public MutableGame clone() {
		return clone(true);
	}

	public MutableGame clone(boolean includeHistory) {
		return new MutableGame(this, includeHistory);
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		history = new History();
	}

	@Override
	public String toString() {
		return "Game [\n\tfoundation=" + foundation + ",\n\tstacks=" + table + ",\n\tdeck=" + deck + "\n]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deck == null) ? 0 : deck.hashCode());
		result = prime * result + ((foundation == null) ? 0 : foundation.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		MutableGame other = (MutableGame) obj;
		if (deck == null) {
			if (other.deck != null)
				return false;
		}
		else if (!deck.equals(other.deck))
			return false;
		if (foundation == null) {
			if (other.foundation != null)
				return false;
		}
		else if (!foundation.equals(other.foundation))
			return false;

		if (table == null) {
			if (other.table != null)
				return false;
		}
		else if (!table.equals(other.table))
			return false;
		return true;
	}

	public void print(PrintStream out) {
		out.print("Foundation [");

		for (Suit suit : Suit.values())
			if (!foundation.isEmpty(suit))
				out.print(" " + foundation.getTopCard(suit) + " ");

		out.print("]\n------------------------------------\n");

		int height = 0;
		for (MutableStack stack : table)
			if (stack.getPile().getAll().size() > height)
				height = stack.getPile().getAll().size();

		for (int index = 0; index < height; index++) {
			for (MutableStack stack : table) {
				Pile pile = stack.getPile();
				if (index >= pile.getAll().size()) {
					out.print("     ");
					continue;
				}

				Card card = pile.getAll().get(index >= pile.getFirstVisibleCardIndex() ? pile.getAll().size() - (index - pile.getFirstVisibleCardIndex()) - 1 : index);
				if (pile.getVisible().contains(card))
					out.print(" " + card + "  ");
				else
					out.print("[" + card + "] ");
			}

			out.println();
		}

		out.print("--------------------------------------------------------\n");

		out.print("Deck [ ");

		for (Card card : deck.getCards().getHidden())
			out.print(card + " ");
		out.print("| ");
		for (Card card : deck.getCards().getVisible())
			out.print(card + " ");

		out.println("]");
	}

}
