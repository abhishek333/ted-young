package me.tedyoung.solitaire.framework.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.google.common.base.Stopwatch;

public class SwingProgressBar extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final Listener listener;

	private final GroupLayout layout;

	private final JProgressBar bar;

	private final JLabel complete = new JLabel("", JLabel.RIGHT);
	private final JLabel time = new JLabel("", JLabel.RIGHT);
	private final JLabel rate = new JLabel("", JLabel.RIGHT);
	private final JLabel last = new JLabel("", JLabel.RIGHT);
	private final JLabel uptime = new JLabel("", JLabel.RIGHT);
	private final JLabel active = new JLabel("", JLabel.RIGHT);

	private final JButton pause = new JButton("Pause"), summary = new JButton("Summarize");
	private boolean paused = false;

	private Stopwatch stopwatch = new Stopwatch();
	private static final PeriodFormatter formatter = new PeriodFormatterBuilder().appendHours().appendSuffix("h ").appendMinutes().appendSuffix("m ").appendSeconds().appendSuffix("s").toFormatter();

	public SwingProgressBar(Listener listener) {
		super("Progress...");

		this.listener = listener;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GridBagLayout grid = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(grid);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridwidth = 500;
		constraints.gridx = 0;

		bar = new JProgressBar();
		bar.setPreferredSize(new Dimension(200, 18));
		constraints.gridy = 1;
		add(bar, constraints);

		JPanel pane = new JPanel();
		layout = new GroupLayout(pane);
		pane.setLayout(layout);
		constraints.gridy = 2;
		add(pane, constraints);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(false);

		JLabel completeLabel = new JLabel("Completed:");
		JLabel timeLabel = new JLabel("Time Remaining:");
		JLabel rateLabel = new JLabel("Average Rate:");
		JLabel lastLabel = new JLabel("Last Test:");
		JLabel uptimeLabel = new JLabel("Uptime:");
		JLabel activeLabel = new JLabel("Scheduled/Active:");

		GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
		horizontal.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(completeLabel).addComponent(uptimeLabel).addComponent(timeLabel).addComponent(rateLabel).addComponent(lastLabel).addComponent(activeLabel));
		horizontal.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(complete, 200, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE).addComponent(uptime).addComponent(time).addComponent(rate).addComponent(last).addComponent(active));
		layout.setHorizontalGroup(horizontal);

		GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(completeLabel).addComponent(complete));
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(rateLabel).addComponent(rate));
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(timeLabel).addComponent(time));
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lastLabel).addComponent(last));
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(uptimeLabel).addComponent(uptime));
		vertical.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(activeLabel).addComponent(active));
		layout.setVerticalGroup(vertical);

		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		pane = new JPanel();
		add(pane, constraints);

		pane.add(summary);
		pane.add(pause);
		pause.addActionListener(this);
		summary.addActionListener(this);

		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == summary) {
			listener.summarize();
		}
		else {
			if (paused) {
				paused = false;
				stopwatch.start();
				listener.resume();
				pause.setText("Pause");
			}
			else {
				paused = true;
				stopwatch.stop();
				listener.pause();
				pause.setText("Resume");
			}
		}
	}

	public void start() {
		stopwatch.start();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(true);
			}
		});
	}

	public void stop() {
		stopwatch.stop();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(false);
				dispose();
			}
		});
	}

	public void setMaximum(final int maximum) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bar.setMaximum(maximum);
			}
		});
	}

	public void setValue(final long progress, final long scheduled, final long queued) {
		final long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		final double average = elapsed / (double) progress;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				rate.setText(String.format("%.3fs", average / 1000));
				last.setText(String.format("%tT", new Date()));

				bar.setValue((int) progress);
				int maximum = bar.getMaximum();
				complete.setText(String.format("%,5d / %,d (%3.1f%%)", progress, maximum, 100 * progress / (double) maximum));
				active.setText(String.format("%d / %d", scheduled, queued));
				uptime.setText(formatter.print(new Period(elapsed)));

				try {
					time.setText(formatter.print(new Period((long) ((maximum - progress) * average))));
				}
				catch (ArithmeticException e) {
				}
			}
		});
	}

	public static interface Listener {
		void pause();

		void resume();

		void summarize();
	}

}
