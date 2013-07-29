package me.tedyoung.solitaire.game;

import java.util.List;

public interface Table<S extends Stack> extends Iterable<S> {

	List<S> getStacks();

	S getStack(int index);

	Stack getStackContainingVisibleCard(Card card);

	boolean isPlayable(Card card);

	List<S> getPlayableStacks(Card card);

	S getFirstEmptyStack();

	int getSize();

}
