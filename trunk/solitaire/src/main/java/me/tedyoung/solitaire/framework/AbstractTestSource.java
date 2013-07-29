package me.tedyoung.solitaire.framework;

import com.google.common.collect.AbstractIterator;

public abstract class AbstractTestSource extends AbstractIterator<Test> implements TestSource {
	protected final int size;

	public AbstractTestSource(int size) {
		this.size = size;
	}

	@Override
	public int size() {
		return size;
	}
}
