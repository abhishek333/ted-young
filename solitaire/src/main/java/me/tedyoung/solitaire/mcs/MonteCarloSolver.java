package me.tedyoung.solitaire.mcs;

import static me.tedyoung.solitaire.mcs.MonteCarloHeuristic.WON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tedyoung.solitaire.DeadLockDetector;
import me.tedyoung.solitaire.framework.Abort;
import me.tedyoung.solitaire.framework.AbstractScoringPlayer;
import me.tedyoung.solitaire.framework.ChainablePlayer;
import me.tedyoung.solitaire.framework.GameResult;
import me.tedyoung.solitaire.framework.Player;
import me.tedyoung.solitaire.framework.heuristic.AdvancedMoveHeuristic;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.utilities.GameCache;
import me.tedyoung.solitaire.utilities.PlayerRunControl;

public class MonteCarloSolver extends AbstractScoringPlayer implements ChainablePlayer {
	protected String name = "MCS";

	protected Lookahead lookahead;

	protected boolean revised;

	protected boolean useDeadlockDetection = true;

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

		this.revised = revised;
		MonteCarloMoveSource source = new MonteCarloMoveSource(revised);
		source.setCheckForDeadLocks(false);
		setMoveSource(source);
		setRunControl(control);

		if (this.heuristics.size() == 1 && this.heuristics.get(0).getEvaluationDepth() == -1)
			setLookahead(new Lookahead(new AdvancedMoveHeuristic(false), null));
		else
			setLookahead(new Lookahead(new ClosingHeuristic(-1), new Lookahead(new AdvancedMoveHeuristic(false), null)));
	}

	public MonteCarloSolver(PlayerRunControl control, MonteCarloHeuristic... heuristics) {
		this.heuristics.addAll(Arrays.asList(heuristics));
		this.revised = false;
		MonteCarloMoveSource source = new MonteCarloMoveSource(revised);
		source.setCheckForDeadLocks(false);
		setMoveSource(source);
		setRunControl(control);

		if (this.heuristics.size() == 1 && this.heuristics.get(0).getEvaluationDepth() == -1)
			setLookahead(new Lookahead(new AdvancedMoveHeuristic(false), null));
		else
			setLookahead(new Lookahead(new ClosingHeuristic(-1), new Lookahead(new AdvancedMoveHeuristic(false), null)));
	}

	@Override
	public void chainedTo(Player player) {
		if (player instanceof MonteCarloSolver) {
			((MonteCarloSolver) player).cache = this.cache;
			this.useDeadlockDetection = false;
		}
	}

	@Override
	public void pause(Game game) {
		if (lookahead != null)
			lookahead.pause(game);
		super.pause(game);
	}

	@Override
	public void resume(Game game) {
		if (lookahead != null)
			lookahead.resume(game);
		super.resume(game);
	}

	@Override
	public GameResult playGame(Game game) {
		if (isBlocked((MutableGame) game))
			return GameResult.LOST;
		return super.playGame(game);
	}

	private boolean isBlocked(MutableGame game) {
		if (revised && useDeadlockDetection)
			return !new DeadLockDetector().isSolvable(game);
		else
			return false;
	}

	@Override
	public Move chooseMove(Game g, List<Move> moves) {
		if (lookahead != null) {
			MutableGame game = (MutableGame) g;
			int mark = game.mark();
			if (lookahead.playGame(game) == GameResult.WON)
				throw new Abort.Complete();
			game.restore(mark);
		}

		return super.chooseMove(g, moves);
	}

	@Override
	protected int score(Move move, Game game, List<Move> moves) {
		MutableGame mutableGame = (MutableGame) game;
		mutableGame.play(move);

		// At 3/1, improved performance 163%, didn't affect success
		int offset = 0;
		if (moves.indexOf(move) > moves.size() / 2)
			offset = 1;
		if (offset < -1)
			offset = -1;

		int score = score(mutableGame, 0, heuristics.get(0).getEvaluationDepth() - offset);
		mutableGame.undo();
		return score;
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
		int score = MonteCarloHeuristic.LOST;

		for (Move move : moves) {
			game.play(move);
			int result = score(game, heuristic, depth - 1);
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

	@Override
	public void setRunControl(PlayerRunControl control) {
		super.setRunControl(control);
		if (this.lookahead != null)
			this.lookahead.setRunControl(control);
	}

	public void setHeuristics(List<MonteCarloHeuristic> heuristics) {
		this.heuristics = heuristics;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUseDeadlockDetection(boolean useDeadlockDetection) {
		this.useDeadlockDetection = useDeadlockDetection;
	}

}
