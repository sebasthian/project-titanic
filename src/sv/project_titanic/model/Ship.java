package sv.project_titanic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Ship implements Serializable {
	private ArrayList<Coordinate> coords;
	private ArrayList<Coordinate> live_coords;
	
	public Ship(ArrayList<Coordinate> coords){
		this.coords = new ArrayList<>(coords);
		live_coords = new ArrayList<>(coords);
	}

	public ArrayList<Coordinate> getCoords() {
		return coords;
	}
	
	public void shipHit(Coordinate coord) {
		live_coords.remove(coord);
	}
	
	public boolean noMoreShip() {
		return live_coords.isEmpty();
	}
	
	public boolean hasCoordinate(Coordinate coord) {
		return live_coords.contains(coord);
	}
}

