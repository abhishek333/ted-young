package me.tedyoung.solitaire.tester.group;

import java.util.Collection;

import me.tedyoung.solitaire.tester.Dependency;

public abstract class Group extends Dependency {
	public static Group allOf(Dependency... dependencies) {
		return new Conjunction(dependencies);
	}

	public static Group allOf(Collection<? extends Dependency> dependencies) {
		return new Conjunction(dependencies);
	}

	public static Group anyOf(Dependency... dependencies) {
		return new Disjunction(dependencies);
	}

	public static Group anyOf(Collection<? extends Dependency> dependencies) {
		return new Disjunction(dependencies);
	}

	public abstract boolean isEmpty();
}
