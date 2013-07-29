package me.tedyoung.solitaire.tester;

import java.util.Set;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.tester.group.Conjunction;
import me.tedyoung.solitaire.tester.group.Disjunction;
import me.tedyoung.solitaire.tester.group.Group;

public abstract class Dependency {
	public static Dependency SUCCEED = new Constant(true);
	public static Dependency FAIL = new Constant(false);

	public abstract boolean isConstant();

	public abstract boolean isFreeOfCycle(SearchContext context);

	public abstract Set<Card> getCards();

	public Group and(Dependency dependency) {
		return new Conjunction(this, dependency);
	}

	public Group or(Dependency dependency) {
		return new Disjunction(this, dependency);
	}
}
