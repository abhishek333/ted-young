package me.tedyoung.solitaire.tester.group;

import java.util.Collections;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.tester.Dependency;
import me.tedyoung.solitaire.tester.SearchContext;

@Deprecated
public class Scalar extends Group {
	private final Dependency dependency;

	public Scalar(Dependency dependency) {
		if (dependency instanceof Scalar)
			this.dependency = ((Scalar) dependency).getDependency();
		else
			this.dependency = dependency;
	}

	@Override
	public boolean isConstant() {
		return dependency.isConstant();
	}

	@Override
	public boolean isFreeOfCycle(SearchContext context) {
		return dependency.isFreeOfCycle(context);
	}

	public Dependency getDependency() {
		return dependency;
	}

	@Override
	public String toString() {
		return dependency.toString();
	}

	@Override
	public boolean isEmpty() {
		return dependency == null;
	}

	@Override
	public Set<Card> getCards() {
		return isEmpty() ? Collections.<Card> emptySet() : dependency.getCards();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependency == null) ? 0 : dependency.hashCode());
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
		Scalar other = (Scalar) obj;
		if (dependency == null) {
			if (other.dependency != null)
				return false;
		}
		else if (!dependency.equals(other.dependency))
			return false;
		return true;
	}
}
