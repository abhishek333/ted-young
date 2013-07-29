package me.tedyoung.solitaire.game;

public class SavedState {
	private MutableDeck deck;

	private MutableFoundation foundation;

	private MutableTable table;

	private Integer hashCode;

	private SavedState() {
	}

	public SavedState(MutableGame game) {
		this(game, true);
	}

	public SavedState(MutableGame game, boolean considerDeckPosition) {
		deck = game.getDeck().clone();

		if (!considerDeckPosition)
			deck.flip();

		foundation = game.getFoundation().clone();
		table = game.getTable().clone();
	}

	public static SavedState proxy(MutableGame game) {
		SavedState state = new SavedState();
		state.deck = game.getDeck();
		state.foundation = game.getFoundation();
		state.table = game.getTable();
		return state;
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((deck == null) ? 0 : deck.hashCode());
			result = prime * result + ((foundation == null) ? 0 : foundation.hashCode());
			result = prime * result + ((table == null) ? 0 : table.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SavedState other = (SavedState) obj;

		if (hashCode() != other.hashCode())
			return false;

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

	public MutableDeck getDeck() {
		return deck;
	}

	public MutableFoundation getFoundation() {
		return foundation;
	}

	public MutableTable getTable() {
		return table;
	}

	@Override
	public String toString() {
		return "SavedState [deck=" + deck + ", foundation=" + foundation + ", table=" + table + "]";
	}
}
