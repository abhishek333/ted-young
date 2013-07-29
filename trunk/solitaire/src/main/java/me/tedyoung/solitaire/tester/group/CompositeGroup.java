package me.tedyoung.solitaire.tester.group;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.tester.Dependency;
import me.tedyoung.solitaire.tester.SearchContext;
import me.tedyoung.solitaire.utilities.CollectionUtilities;

public abstract class CompositeGroup extends Group {
	protected final Set<Dependency> dependencies = new LinkedHashSet<>();
	protected Boolean value;
	private Boolean cache;

	public CompositeGroup(Dependency... dependencies) {
		this(Arrays.asList(dependencies));
	}

	public CompositeGroup(Collection<? extends Dependency> dependencies) {
		for (Dependency dependency : dependencies) {
			if (dependency == null)
				continue;
			else if (dependency.getClass() == getClass())
				this.dependencies.addAll(((CompositeGroup) dependency).dependencies);
			else
				this.dependencies.add(dependency);
		}

		for (Iterator<Dependency> iterator = this.dependencies.iterator(); iterator.hasNext();) {
			Dependency value = iterator.next();
			if (!value.isConstant())
				continue;

			if (value.isFreeOfCycle(null) == defaultValue()) {
				iterator.remove();
			}
			else {
				makeConstant(!defaultValue());
				return;
			}
		}

		if (this.dependencies.isEmpty())
			makeConstant(defaultValue());
	}

	@Override
	public boolean isConstant() {
		return value != null;
	}

	@Override
	public boolean isEmpty() {
		return dependencies.isEmpty();
	}

	@Override
	public boolean isFreeOfCycle(SearchContext context) {
		if (isConstant())
			return value;

		if (cache != null)
			return cache;

		boolean result = areDependenciesFreeOfCycle(context);

		if (result)
			cache = result;

		return result;
	}

	protected void makeConstant(boolean value) {
		this.value = value;
	}

	protected abstract boolean areDependenciesFreeOfCycle(SearchContext context);

	protected abstract boolean defaultValue();

	protected String toString(String delimeter) {
		if (isConstant())
			return isFreeOfCycle(null) ? "+" : "-";

		if (dependencies.size() == 1)
			return dependencies.iterator().next().toString();

		return "(" + CollectionUtilities.join(delimeter, dependencies) + ")";
	}

	@Override
	public Set<Card> getCards() {
		HashSet<Card> cards = new HashSet<>();
		for (Dependency dependency : dependencies)
			cards.addAll(dependency.getCards());
		return cards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
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
		CompositeGroup other = (CompositeGroup) obj;
		if (dependencies == null) {
			if (other.dependencies != null)
				return false;
		}
		else if (!dependencies.equals(other.dependencies))
			return false;
		return true;
	}

}
