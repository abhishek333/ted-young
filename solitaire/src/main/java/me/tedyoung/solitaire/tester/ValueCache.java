package me.tedyoung.solitaire.tester;

public class ValueCache {
	private Boolean primary, secondary;

	public boolean isCached(boolean version) {
		return (version ? primary : secondary) != null;
	}

	public boolean getValue(boolean version) {
		return version ? primary : secondary;
	}

	public void setValue(boolean version, boolean value) {
		if (version)
			primary = value;
		else
			secondary = value;
	}
}
