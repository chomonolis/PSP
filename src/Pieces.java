public class Pieces {
	private int x, y;
	private boolean isEmpty = false;

	public Pieces(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Pieces(int x, int y, boolean f) {
		this(x, y);
		this.isEmpty = f;
	}

	public boolean getIsEmpty() {
		return this.isEmpty;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
