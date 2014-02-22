package sv.project_titanic.model;

public class Coordinate {
	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coordinate) {
			Coordinate other = (Coordinate)obj;
			return this.x == other.x && this.y == other.y;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (x*31) ^ y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

