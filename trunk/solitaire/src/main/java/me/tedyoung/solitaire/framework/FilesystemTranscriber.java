package me.tedyoung.solitaire.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;
import me.tedyoung.solitaire.game.move.DeckToFoundationMove;
import me.tedyoung.solitaire.game.move.DeckToStackMove;
import me.tedyoung.solitaire.game.move.FoundationToStackMove;
import me.tedyoung.solitaire.game.move.Move;
import me.tedyoung.solitaire.game.move.StackToFoundationMove;
import me.tedyoung.solitaire.game.move.StackToStackMove;

public class FilesystemTranscriber implements Transcriber {
	private int count, wins;

	private PrintWriter out;

	public FilesystemTranscriber(String name, String path) throws FileNotFoundException {
		Date date = new Date();
		out = new PrintWriter(new File(path, String.format("%s %tF.txt", name, date)));
		out.printf("%s %tF %tT", name, date, date);
		out.println();
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		out.printf("%d/%d", wins, count);
		out.println();
		out.close();
	}

	@Override
	public synchronized void transcribe(MutableGame game) {
		if (game.isComplete()) {
			out.print("W");
			wins++;
		}
		else {
			out.print("L");
		}
		count++;

		List<Move> moves = reverse(game.getHistory().getMoves());
		game.reset();

		for (MutableStack stack : game.getTable())
			for (Card card : reverse(stack.getAllCards()))
				out.print(card);

		for (Card card : reverse(game.getDeck().getAllCards()))
			out.print(card);

		for (Move move : moves) {
			out.print(move.getCard());
			if (move instanceof StackToStackMove)
				out.print(((StackToStackMove) move).getDestination());
			else if (move instanceof DeckToStackMove)
				out.print(((DeckToStackMove) move).getStack());
			else if (move instanceof FoundationToStackMove)
				out.print(((FoundationToStackMove) move).getStack());
			else if (move instanceof StackToFoundationMove || move instanceof DeckToFoundationMove)
				out.print("F");
		}

		out.println();
	}

	private <T> List<T> reverse(List<T> list) {
		ArrayList<T> reversed = new ArrayList<>(list);
		Collections.reverse(reversed);
		return reversed;
	}
}
