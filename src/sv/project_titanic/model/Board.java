package sv.project_titanic.model;

import java.util.ArrayList;
import java.util.Observable;

import sv.project_titanic.model.Ship;

public class Board extends Observable {
	private int width;
	private int height;
	private ArrayList<Ship> fleet;
	public int[][] fieldStatus;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		
		fleet = new ArrayList<>();
		fieldStatus = new int[width][height];
	}

	public void addShip(Ship ship) {
		fleet.add(ship);
	}

	public void setFieldStatus(int x, int y, int status) {
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
	}
	
	public int getXdim() {
		return width;
	}
	
	public int getYdim() {
		return height;
	}
	
	public int getFieldStatus(int x, int y) {
		return fieldStatus[x][y];
	}

	public ArrayList<Ship> getFleet() {
		return fleet;
	}

	public Ship getShipByCoord(Coordinate coord) {
		for(Ship ship : fleet) {
			if(ship.hasCoordinate(coord))
				return ship;
		}

		return null;
	}
}

