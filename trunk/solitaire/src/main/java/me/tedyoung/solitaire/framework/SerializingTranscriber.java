package me.tedyoung.solitaire.framework;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import me.tedyoung.solitaire.game.MutableGame;

public class SerializingTranscriber implements Transcriber {
	private ObjectOutputStream out;

	private int count;
	private int handSize = 3;
	private File temp;
	private File file;

	public SerializingTranscriber(String name) throws IOException {
		temp = new File(name + ".tmp");
		file = new File(name);
		out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(temp)));
	}

	@Override
	public synchronized void transcribe(MutableGame game, Test test) {
		try {
			if (!includeGame(game, test))
				return;
			handSize = game.getDeck().getHandSize();
			game.reset();
			out.writeObject(game);
			count++;
		}
		catch (IOException e) {
			new RuntimeException(e);
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		try {
			out.close();
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			out.writeInt(count);
			out.writeInt(handSize);

			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(temp)));
			while (count-- > 0)
				out.writeObject(in.readObject());

			in.close();
			out.close();
			temp.delete();
		}
		catch (IOException | ClassNotFoundException e) {
			new RuntimeException(e);
		}
	}

	public boolean includeGame(MutableGame game, Test test) {
		return true;
	}
}
