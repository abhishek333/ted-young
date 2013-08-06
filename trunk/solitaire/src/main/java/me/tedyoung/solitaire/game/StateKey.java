package me.tedyoung.solitaire.game;

import java.util.Arrays;
import java.util.List;

public class StateKey {
	private byte[] value = new byte[52];
	private int hash = 0;

	public StateKey(MutableGame game) {
		write(game.getDeck().getAllCards());
		write(game.getDeck().getHiddenCards().size());

		for (MutableStack stack : game.getTable()) {
			write(stack.getAllCards());
			write(stack.getHiddenCards().size());
		}

		for (Card card : game.getFoundation().getTopCards())
			write(card);

		hash = Arrays.hashCode(value);
	}

	private void write(List<Card> cards) {
		write(cards.size());
		for (Card card : cards)
			write(card);
	}

	private void write(Card card) {
		write(card.ordinal());
	}

	private void write(int value) {
		this.value[hash++] = (byte) value;
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
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}

}
