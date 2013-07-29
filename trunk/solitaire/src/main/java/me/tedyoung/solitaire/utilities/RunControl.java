package me.tedyoung.solitaire.utilities;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RunControl {
	private volatile boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();

	public void verify() {
		if (!isPaused)
			return;

		pauseLock.lock();
		try {
			while (isPaused)
				unpaused.await();
		}
		catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
		finally {
			pauseLock.unlock();
		}
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void pause() {
		if (isPaused)
			return;

		pauseLock.lock();
		try {
			isPaused = true;
		}
		finally {
			pauseLock.unlock();
		}
	}

	public void resume() {
		if (!isPaused)
			return;

		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		}
		finally {
			pauseLock.unlock();
		}
	}
}
