package me.tedyoung.solitaire.game;

public enum Denomination {
	ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("T"), JACK("J"), QUEEN("Q"), KING("K");

	private final String symbol;

	private Denomination(String symbol) {
		this.symbol = symbol;
	}

	public int getValue() {
		return ordinal() + 1;
	}

	public boolean isOneLowerThan(Denomination that) {
		return this.ordinal() == that.ordinal() - 1;
	}

	public boolean isOneGreaterThan(Denomination that) {
		return this.ordinal() == that.ordinal() + 1;
	}

	public Denomination previous() {
		if (this == ACE)
			return null;
		else
			return values()[ordinal() - 1];
	}

	public Denomination next() {
		if (this == KING)
			return null;
		else
			return values()[ordinal() + 1];
	}

	@Override
	public String toString() {
		return symbol;
	}
}
