package me.tedyoung.solitaire.utilities;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
	private RunControl control = new RunControl();

	public PausableThreadPoolExecutor(int threads) {
		super(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		control.verify();
	}

	public void pause() {
		control.pause();
	}

	public void resume() {
		control.resume();
	}
}
