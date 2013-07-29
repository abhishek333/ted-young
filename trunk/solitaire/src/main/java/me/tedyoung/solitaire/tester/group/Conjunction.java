package me.tedyoung.solitaire.tester.group;

import java.util.Collection;

import me.tedyoung.solitaire.tester.Dependency;
import me.tedyoung.solitaire.tester.SearchContext;

public class Conjunction extends CompositeGroup {
	public Conjunction() {
		super();
	}

	public Conjunction(Dependency... dependencies) {
		super(dependencies);
	}

	public Conjunction(Collection<? extends Dependency> dependencies) {
		super(dependencies);
	}

	@Override
	protected boolean defaultValue() {
		return true;
	}

	@Override
	protected boolean areDependenciesFreeOfCycle(SearchContext context) {
		for (Dependency dependency : dependencies)
			if (!dependency.isFreeOfCycle(context))
				return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString(" & ");
	}
}
