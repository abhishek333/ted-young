package me.tedyoung.solitaire.mcs;

import static me.tedyoung.solitaire.mcs.MonteCarloHeuristic.LOST;
import static me.tedyoung.solitaire.mcs.MonteCarloHeuristic.WON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tedyoung.solitaire.framework.AbstractScoringPlayer;
import me.tedyoung.solitaire.framework.ChainablePlayer;
import me.tedyoung.solitaire.framework.GameResult;
import me.tedyoung.solitaire.framework.Player;
import me.tedyoung.solitaire.framework.Tester;
import me.tedyoung.solitaire.framework.heuristic.AdvancedMoveHeuristic;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.tester.DeadlockTester;
import me.tedyoung.solitaire.utilities.GameCache;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class MonteCarloSolver extends AbstractScoringPlayer implements ChainablePlayer {
	protected String name = "MCS";
	protected Lookahead lookahead;
	protected Tester tester;
	protected boolean revised;
	protected List<MonteCarloHeuristic> heuristics = new ArrayList<>();
	private GameCache<CacheEntry, Integer> cache = new GameCache<CacheEntry, Integer>(100_000, Integer.MIN_VALUE);

	public MonteCarloSolver(Integer opening, Integer closing, PlayerRunControl control) {
		this(opening, closing, control, false);
	}

	public MonteCarloSolver(Integer opening, Integer closing, PlayerRunControl control, boolean revised) {
		if (opening != null)
			heuristics.add(new OpeningHeuristic(opening));

		if (closing != null)
			heuristics.add(new ClosingHeuristic(closing));

		initialize(revised, control);
	}

	public MonteCarloSolver(PlayerRunControl control, MonteCarloHeuristic... heuristics) {
		this(false, control, heuristics);
	}

	public MonteCarloSolver(boolean revised, PlayerRunControl control, MonteCarloHeuristic... heuristics) {
		this.heuristics.addAll(Arrays.asList(heuristics));
		initialize(revised, control);
	}

	private void initialize(boolean revised, PlayerRunControl control) {
		this.revised = revised;
		setRunControl(control);

		MonteCarloMoveSource source = new MonteCarloMoveSource(revised);
		setMoveSource(source);

		if (maximumHeuristicEvaluationDepth() == -1)
			setLookahead(new Lookahead(new AdvancedMoveHeuristic(), null));
		else
			setLookahead(new Lookahead(new ClosingHeuristic(-1), new Lookahead(new AdvancedMoveHeuristic(), null)));

		if (totalHeuristicEvaluationDepth() > 3)
			setTester(new DeadlockTester(control));
	}

	private int maximumHeuristicEvaluationDepth() {
		int max = Integer.MIN_VALUE;
		for (MonteCarloHeuristic heuristic : heuristics)
			if (heuristic.getEvaluationDepth() > max)
				max = heuristic.getEvaluationDepth();
		return max;
	}

	private int totalHeuristicEvaluationDepth() {
		int sum = 0;
		for (MonteCarloHeuristic heuristic : heuristics)
			sum += heuristic.getEvaluationDepth();
		return sum;
	}

	@Override
	public void chainedTo(Player player) {
		if (player instanceof MonteCarloSolver) {
			MonteCarloSolver that = (MonteCarloSolver) player;
			that.cache = this.cache;
			if (tester != null) {
				that.tester = tester;
				this.tester = null;
			}
		}
	}

	@Override
	public GameResult playGame(Game game) {
		if (tester != null && !tester.isSolvable(game))
			return GameResult.LOST;
		return super.playGame(game);
	}

	@Override
	public Move chooseMove(Game g, List<Move> moves) {
		MutableGame game = (MutableGame) g;
		if (lookahead != null)
			lookahead.playGame(game);
		return super.chooseMove(g, moves);
	}

	@Override
	protected int score(Move move, Game game, List<Move> moves) {
		MutableGame mutableGame = (MutableGame) game;
		mutableGame.play(move);

		// At 3/1, improved performance 163%, didn't affect success
		int depth = weightedDepth(heuristics.get(0).getEvaluationDepth(), move, moves);
		int score = score(mutableGame, 0, depth);
		mutableGame.undo();
		return score;
	}

	protected int weightedDepth(int depth, Move move, List<Move> moves) {
		if (depth > -1 && moves.indexOf(move) > moves.size() / 2)
			depth--;
		return depth;
	}

	protected int score(MutableGame game, int heuristic, int depth) {
		if (depth > -1) {
			CacheEntry node = new CacheEntry(game.getSavedState(), heuristics.get(heuristic), depth);
			Integer score = cache.get(game, node);
			if (score > Integer.MIN_VALUE)
				return score;

			score = scoreImpl(game, heuristic, depth);
			cache.set(game, node, score);
			return score;
		}
		else
			return scoreImpl(game, heuristic, depth);
	}

	protected int scoreImpl(MutableGame game, int heuristic, int depth) {
		int value = heuristics.get(heuristic).valueOf(game);

		if (depth == -1)
			return value;

		if (game.isComplete())
			return WON;

		verify(game);

		List<Move> moves = getMoves(game);
		if (moves.isEmpty())
			return value;

		Move best = null;
		int score = LOST;

		for (Move move : moves) {
			game.play(move);
			int result = score(game, heuristic, revised ? weightedDepth(depth - 1, move, moves) : depth - 1);
			if (result > score) {
				score = result;
				best = move;
			}
			game.undo();
		}

		if (score == WON)
			return WON;

		if (value > score && heuristic < heuristics.size() - 1) {
			score = score(game, heuristic + 1, heuristics.get(heuristic + 1).getEvaluationDepth());
		}
		else {
			game.play(best);
			score = score(game, heuristic, depth);
			game.undo();
		}

		return score;
	}

	@Override
	public String getName() {
		return name + ": " + heuristics + (revised ? "*" : "");
	}

	public void setRevised(boolean revised) {
		this.revised = revised;
	}

	public void setLookahead(Lookahead lookahead) {
		this.lookahead = lookahead;
		if (this.lookahead != null)
			this.lookahead.setRunControl(getRunControl());
	}

	public void setTester(Tester tester) {
		this.tester = tester;
		if (this.tester != null)
			this.tester.setRunControl(getRunControl());
	}

	@Override
	public void setRunControl(PlayerRunControl control) {
		super.setRunControl(control);
		if (this.lookahead != null)
			this.lookahead.setRunControl(control);
		if (this.tester != null)
			this.tester.setRunControl(control);
	}

	public void setHeuristics(List<MonteCarloHeuristic> heuristics) {
		this.heuristics = heuristics;
	}

	public void setName(String name) {
		this.name = name;
	}

}
