package sv.project_titanic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import sv.project_titanic.model.Ship;

/**Game board representation. Each cell is represented as an integer status
 * in a 2D-array. Keeps references to ships placed on the board. Can be
 * observed.
 *
 * @see java.util.Observable
 * @see java.util.Observer
 */
public class Board extends Observable implements Serializable {
	private int width;
	private int height;
	private ArrayList<Ship> fleet;
	public int[][] fieldStatus;

	/**Create a new board of the specified size.
	 *
	 * @param width the horizontal size.
	 * @param height the vertical size.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		
		fleet = new ArrayList<>();
		fieldStatus = new int[width][height];
	}

	/**Copy the data from another board into this board. Used to initialize
	 * the opponent's board when starting a new game. Ships are copied in a
	 * shallow way.
	 *
	 * @param other the Board to copy data from.
	 */
	public void copyBoard(Board other) {
		fleet = new ArrayList<>(other.fleet);
		width = other.width;
		height = other.height;

		fieldStatus = new int[width][height];

		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++)
				fieldStatus[i][j] = other.fieldStatus[i][j];
		}
	}

	/**
	 * @param ship a Ship to add to this board.
	 */
	public void addShip(Ship ship) {
		fleet.add(ship);
	}

	/**Change the status of a cell on the board. Notify all registered
	 * Observers, sending an array of the form {x, y, status}.
	 *
	 * @param x x-coordinate of the cell.
	 * @param y y-coordinate of the cell.
	 * @param status The new status. Should be in [0, 4].
	 */
	public void setFieldStatus(int x, int y, int status) {
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
	}
	
	/**
	 * @return the horizontal size of this board.
	 */
	public int getXdim() {
		return width;
	}
	
	/**
	 * @return the vertical size of this board.
	 */
	public int getYdim() {
		return height;
	}
	
	/**
	 * @param x x-coordinate of the cell.
	 * @param y y-coordinate of the cell.
	 *
	 * @return the status of a cell on the board.
	 */
	public int getFieldStatus(int x, int y) {
		return fieldStatus[x][y];
	}

	/**
	 * @return the list of Ships currently placed on this board.
	 */
	public ArrayList<Ship> getFleet() {
		return fleet;
	}

	/**Find the Ship, if any, that is located on a specific coordinate.
	 *
	 * @param coord where to look for a Ship.
	 *
	 * @return a Ship that contains the specified coordinate, or null if no
	 *         Ship found.
	 */
	public Ship getShipByCoord(Coordinate coord) {
		for(Ship ship : fleet) {
			if(ship.hasCoordinate(coord))
				return ship;
		}

		return null;
	}
}

