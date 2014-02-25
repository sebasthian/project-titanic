package sv.project_titanic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

/**Representation of a ship on the board. Keeps track of what coordinates
 * it is located at, and if those coordinates have been hit by the opponent.
 */
public class Ship implements Serializable {
	private ArrayList<Coordinate> coords;
	private ArrayList<Coordinate> live_coords;

	/**Create a new ship.
	 *
	 * @param coords Where on the board this Ship is located.
	 */
	public Ship(ArrayList<Coordinate> coords){
		this.coords = new ArrayList<>(coords);
		live_coords = new ArrayList<>(coords);
	}

	/**@return a list of Coordinates that this Ship occupies.*/
	public ArrayList<Coordinate> getCoords() {
		return coords;
	}
	
	/**Mark a Coordinate as hit by the opponent.
	 *
	 * @param coord the Coordinate to mark.
	 */
	public void shipHit(Coordinate coord) {
		live_coords.remove(coord);
	}
	
	/**@return true if this Ship has been sunk.*/
	public boolean noMoreShip() {
		return live_coords.isEmpty();
	}
	
	/**Check if this Ship occupies a Coordinate.
	 *
	 * @param coord the Coordinate to check.
	 *
	 * @return true if this Ship occupies the Coordinate, false otherwise.
	 */
	public boolean hasCoordinate(Coordinate coord) {
		return live_coords.contains(coord);
	}
}

