package sv.project_titanic.model;

import java.util.ArrayList;
import java.util.Observable;

import sv.project_titanic.model.Ship;

public class Board extends Observable {
	private int boardX;
	private int boardY;
	private ArrayList<Ship> fleet;
	public int[][] fieldStatus;
	
	public Board(int xdim, int ydim) {
		this.boardX = xdim;
		this.boardY = ydim;
		
		fleet = new ArrayList<Ship>();
		fieldStatus = new int[boardX][boardY];
	}
	
	public void addShip(Ship ship) {
		fleet.add(ship);

		for(Coordinate c : ship.getCoords()) {
			setFieldStatus(c.getX(), c.getY(), 2);
		}
	}
	
	public void setFieldStatus(int x, int y, int status) {
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
	}
	
	public int getXdim() {
		return boardX;
	}
	
	public int getYdim() {
		return boardY;
	}
	
	public int getFieldStatus(int x, int y) {
		return fieldStatus[x][y];
	}

	public ArrayList<Ship> getFleet() {
		return fleet;
	}
}

