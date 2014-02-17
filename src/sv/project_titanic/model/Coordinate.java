package sv.project_titanic.model;

public class Coordinate {
	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Compare this coordinate with another. 
	 */
	public boolean equals(Coordinate other) {
		return this.x == other.x && this.y == other.y;
	}

	/*
	 * Return X
	 */
	public int getX() {
		return x;
	}

	/*
	 * Return Y
	 */
	public int getY() {
		return y;
	}
}

