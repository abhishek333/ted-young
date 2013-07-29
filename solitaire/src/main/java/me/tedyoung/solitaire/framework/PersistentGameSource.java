package me.tedyoung.solitaire.framework;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import me.tedyoung.solitaire.game.Game;

public class PersistentGameSource extends AbstractGameSource {
	private ObjectInputStream in;
	private int count;

	public PersistentGameSource(int size, int handSize, ObjectInputStream in) {
		super(size, handSize);
		this.in = in;
	}

	@Override
	protected void finalize() throws Throwable {
		in.close();
	}

	@Override
	protected Game computeNext() {
		try {
			if (count++ == size)
				return endOfData();
			return (Game) in.readObject();
		}
		catch (ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void save(GameSource source, String filename) throws IOException {
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filename)))) {
			save(source, out);
		}
	}

	public static void save(GameSource source, BufferedOutputStream stream) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(stream)) {
			out.writeInt(source.size());
			out.writeInt(source.getHandSize());
			while (source.hasNext())
				out.writeObject(source.next());
		}
	}

	public static PersistentGameSource load(String filename) throws IOException {
		return load(new BufferedInputStream(new FileInputStream(filename)));
	}

	public static PersistentGameSource load(BufferedInputStream stream) throws IOException {
		ObjectInputStream in = new ObjectInputStream(stream);
		return new PersistentGameSource(in.readInt(), in.readInt(), in);
	}
}
