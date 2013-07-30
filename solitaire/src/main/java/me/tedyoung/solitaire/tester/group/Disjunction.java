package me.tedyoung.solitaire.tester.group;

import java.util.Collection;

import me.tedyoung.solitaire.tester.Dependency;

public class Disjunction extends CompositeGroup {
	public Disjunction(Dependency... dependencies) {
		super(dependencies);
	}

	public Disjunction(Collection<? extends Dependency> dependencies) {
		super(dependencies);
	}

	@Override
	protected boolean defaultValue() {
		return false;
	}

	@Override
	public String toString() {
		return super.toString(" | ");
	}
}
