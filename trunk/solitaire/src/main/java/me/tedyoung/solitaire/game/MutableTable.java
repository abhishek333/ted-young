package me.tedyoung.solitaire.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MutableTable implements Table<MutableStack>, Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<MutableStack> stacks = new ArrayList<>(7);

	public MutableTable(MutableDeck deck) {
		for (int index = 0; index < 7; index++)
			stacks.add(new MutableStack(index, deck.deal(index + 1)));
	}

	private MutableTable(MutableTable table) {
		for (MutableStack stack : table)
			stacks.add(stack.clone());
	}

	@Override
	public List<MutableStack> getStacks() {
		return Collections.unmodifiableList(stacks);
	}

	@Override
	public MutableStack getStack(int index) {
		return stacks.get(index);
	}

	public void setStack(int index, MutableStack stack) {
		stacks.set(index, stack);
	}

	@Override
	public MutableStack getStackContainingVisibleCard(Card card) {
		for (MutableStack stack : stacks)
			if (stack.getVisibleCards().contains(card))
				return stack;
		return null;
	}

	public MutableStack getStackContainingCard(Card card) {
		for (MutableStack stack : stacks)
			if (stack.containsCard(card))
				return stack;
		return null;
	}

	@Override
	public List<MutableStack> getPlayableStacks(Card card) {
		ArrayList<MutableStack> list = new ArrayList<>(2);
		for (MutableStack stack : stacks)
			if (stack.isPlayable(card))
				list.add(stack);
		return list;
	}

	@Override
	public boolean isPlayable(Card card) {
		for (MutableStack stack : stacks)
			if (stack.isPlayable(card))
				return true;
		return false;
	}

	@Override
	public MutableStack getFirstEmptyStack() {
		for (MutableStack stack : stacks)
			if (stack.isEmpty())
				return stack;
		return null;
	}

	public Set<Card> getAllCards() {
		HashSet<Card> cards = new HashSet<>();
		for (MutableStack stack : stacks)
			cards.addAll(stack.getAllCards());
		return cards;
	}

	public int getNumberOfHiddenCards() {
		int count = 0;
		for (MutableStack stack : stacks)
			count += stack.getNumberOfHiddenCards();
		return count;
	}

	@Override
	public int getSize() {
		int count = 0;
		for (Stack stack : stacks)
			count += stack.getSize();
		return count;
	}

	@Override
	public Iterator<MutableStack> iterator() {
		return stacks.iterator();
	}

	@Override
	public MutableTable clone() {
		return new MutableTable(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stacks == null) ? 0 : new HashSet<>(stacks).hashCode());
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
		MutableTable other = (MutableTable) obj;
		if (stacks == null) {
			if (other.stacks != null)
				return false;
		}
		else if (!new HashSet<>(stacks).equals(new HashSet<>(other.stacks)))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return stacks.toString();
	}

}
