package me.tedyoung.solitaire.framework;

public class Abort extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static class Timeout extends Abort {
		private static final long serialVersionUID = 1L;
	}

	public static class Distance extends Abort {
		private static final long serialVersionUID = 1L;
	}

	public static class User extends Abort {
		private static final long serialVersionUID = 1L;
	}

	public static class Complete extends Abort {
		private static final long serialVersionUID = 1L;
	}
}
