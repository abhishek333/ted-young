package me.tedyoung.solitaire.game;

import java.util.Arrays;
import java.util.List;

public class StateKey {
	private static final int SEPERATOR = 0xFF;

	private byte[] value = new byte[52 + 8 * 2];
	private int index = 0;

	public StateKey(MutableGame game) {
		write(game.getDeck().getHiddenCards().size());
		write(game.getDeck().getAllCards());
		write(SEPERATOR);

		for (MutableStack stack : game.getTable()) {
			write(stack.getHiddenCards().size());
			write(stack.getAllCards());
			write(SEPERATOR);
		}

		write(game.getFoundation().getTopCards());
	}

	private void write(List<Card> cards) {
		for (Card card : cards)
			write(card);
	}

	private void write(Card card) {
		write(card.ordinal());
	}

	private void write(int value) {
		this.value[index++] = (byte) value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(value);
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
		StateKey other = (StateKey) obj;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}

}
