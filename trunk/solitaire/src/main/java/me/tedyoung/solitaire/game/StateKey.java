package me.tedyoung.solitaire.game;

import java.util.Arrays;
import java.util.ListIterator;

public class StateKey {
	private byte[] bytes = new byte[52];
	private int hash = 0;

	public StateKey(MutableGame game) {
		int offset = 1;

		for (ListIterator<Card> iterator = game.getDeck().getVisibleCards().listIterator(); iterator.hasNext();)
			bytes[iterator.next().ordinal()] = (byte) (offset + iterator.nextIndex() - 1);

		offset = 25;

		for (ListIterator<Card> iterator = game.getDeck().getHiddenCards().listIterator(); iterator.hasNext();)
			bytes[iterator.next().ordinal()] = (byte) (offset + iterator.nextIndex() - 1);

		offset = 49;

		for (MutableStack stack : game.getTable()) {
			for (ListIterator<Card> iterator = stack.getVisibleCards().listIterator(); iterator.hasNext();)
				bytes[iterator.next().ordinal()] = (byte) (offset + iterator.nextIndex() - 1);

			offset += 13;

			for (ListIterator<Card> iterator = stack.getHiddenCards().listIterator(); iterator.hasNext();)
				bytes[iterator.next().ordinal()] = (byte) (offset + iterator.nextIndex() - 1);

			offset += stack.getIndex();
		}

		hash = Arrays.hashCode(bytes);
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateKey other = (StateKey) obj;
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		return true;
	}

}
