package me.tedyoung.solitaire.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import me.tedyoung.solitaire.game.move.Move;

public class History {
	private LinkedList<SavedState> states = new LinkedList<>();
	private LinkedList<Move> moves = new LinkedList<>();

	public History() {
	}

	public History(History history) {
		addAll(history);
	}

	public void add(SavedState state, Move move) {
		moves.addFirst(move);
		states.addFirst(state);
	}

	public void addAll(History history) {
		states.addAll(history.states);
		moves.addAll(history.moves);
	}

	public SavedState remove() {
		moves.removeFirst();
		return states.removeFirst();
	}

	public SavedState clear() {
		if (size() == 0)
			return null;

		moves.clear();
		SavedState state = states.removeLast();
		states.clear();
		return state;
	}

	public boolean contains(SavedState state) {
		return indexOf(state) > -1;
	}

	public int indexOf(SavedState other) {
		ListIterator<SavedState> iterator = states.listIterator();

		while (iterator.hasNext()) {
			SavedState state = iterator.next();
			if (state.equals(other))
				return iterator.nextIndex() - 1;
			if (state.getDeck().getSize() != other.getDeck().getSize())
				return -1;
		}

		return -1;
	}

	public int size() {
		return states.size();
	}

	public int mark() {
		return size();
	}

	public SavedState restore(SavedState state) {
		int mark = indexOf(state);
		if (mark == -1)
			throw new IllegalArgumentException("State not found in history.");
		return restore(mark);
	}

	public SavedState restore(int mark) {
		if (mark >= size())
			return null;

		moves.subList(0, size() - mark).clear();

		SavedState state = states.get(size() - mark - 1);
		states.subList(0, size() - mark).clear();
		return state;
	}

	public List<Move> getMoves() {
		return Collections.unmodifiableList(moves);
	}

}
