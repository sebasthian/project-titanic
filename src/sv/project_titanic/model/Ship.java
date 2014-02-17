package sv.project_titanic.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Ship {
	private static ArrayList<Coordinate> coords;
	
	public Ship(ArrayList<Coordinate> coords){
		this.coords = new ArrayList<>();

		for(Coordinate c : coords)
			this.coords.add(c);
	}

	public ArrayList<Coordinate> getCoords() {
		return coords;
	}
	
	/**
	 * Checks if coord is a hit on the ship.
	 * Returns nothing but removes the corresponding Coordinate object if hit.
	 */
	public void shipHit(Coordinate coord) {
		coords.remove(coord);
	}
	
	/**
	 * Checks if coords contains any Coordinate objects.
	 * @return True if no Coordinate objects exist in coords.
	 */
	public boolean noMoreShip() {
		return coords.isEmpty();
	}
	
	/**
	 * Returns True if coords contains coord.
	 * @return True if the ship contains the coordinate.
	 */
	public boolean hasCoordinate(Coordinate coord) {
		return coords.contains(coord);
	}
}

