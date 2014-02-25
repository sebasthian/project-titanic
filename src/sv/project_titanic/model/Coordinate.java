package sv.project_titanic.model;

import java.io.Serializable;

/**Simple Coordinate class.*/
public class Coordinate implements Serializable {
	private int x;
	private int y;

	/**Create a new coordinate at (x, y).*/
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**Check if this Coordinate is equal to another object.
	 *
	 * @param obj the Object to compare to this coordinate.
	 *
	 * @return true if obj is a Coordinate and the x- and y-values are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coordinate) {
			Coordinate other = (Coordinate)obj;
			return this.x == other.x && this.y == other.y;
		}

		return false;
	}

	/**Produce a hashcode for this Coordinate. Necessary since the equals()
	 * method has been overridden.
	 */
	@Override
	public int hashCode() {
		return (x*31) ^ y;
	}

	/**@return the x-value of this Coordinate.*/
	public int getX() {
		return x;
	}

	/**@return the y-value of this Coordinate.*/
	public int getY() {
		return y;
	}
}

