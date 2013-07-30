package me.tedyoung.solitaire.tester;

import java.util.EnumSet;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;

public class Constant extends Dependency {
	private final boolean value;

	public Constant(boolean value) {
		this.value = value;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isFreeOfCycle(SearchContext context) {
		return value;
	}

	@Override
	public Set<Card> getCards() {
		return EnumSet.noneOf(Card.class);
	}

	@Override
	public String toString() {
		return value ? "+" : "-";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value ? 1231 : 1237);
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
		Constant other = (Constant) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
