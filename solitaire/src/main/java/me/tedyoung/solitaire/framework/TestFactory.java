package me.tedyoung.solitaire.framework;

import java.io.PrintStream;

public interface TestFactory {
	TestSource getTests();

	void testComplete(Test test);

	void printSummary(PrintStream out, boolean abridged);
}
