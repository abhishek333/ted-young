package me.tedyoung.solitaire;

import java.math.MathContext;
import java.math.RoundingMode;

import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.Suit;

public class ProbabilityTests {
	private static final MathContext ctx = new MathContext(20, RoundingMode.HALF_UP);

	public static void main(String... args) {

		for (Suit suit : Suit.values())
			for (Denomination denomination : Denomination.values())
				System.out.printf("%s(Denomination.%s, Suit.%s),\n", suit.name().charAt(0) + denomination.toString(), denomination.name(), suit.name());

	}
}
