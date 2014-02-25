package sv.project_titanic.model;

import java.util.ArrayList;

import sv.project_titanic.model.Ship;

import java.util.Observable;
import java.io.Serializable;

public class Board extends Observable implements Serializable
{
	private int boardX, boardY;
	private ArrayList<Ship> fleet;
	//private boolean playerTurn;
	public int[][] fieldStatus;
	private ArrayList<Coordinate> coordList;
	
	public Board(int xdim, int ydim)
	{
		this.boardX = xdim;
		this.boardY = ydim;
		
		fleet = new ArrayList<Ship>();
		
		fieldStatus = new int[boardX][boardY];

		for(int i=0 ; i < boardX ; i++){
			for(int j=0 ; j < boardY; j++){
				fieldStatus[i][j] = 0;
			}
		}
		
	}
	
	/**
	 * Adds ships to the fleet.
	 * @param ship	the ship that will be added
	 */
	public void addShip(Ship ship)
	{
		fleet.add(ship);
	}
	
	/**
	 * Changes the status of the coordinate that is sent as the status
	 * and notifys all observers about the change that has been done
	 * 
	 * @param x 	 x-coordinate of the cell that will be changed 
	 * @param y 	 y-coordinate of the cell that will be changed
	 * @param status status that will be set.
	 */
	public void setFieldStatus(int x, int y, int status)
	{
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
	}
	
	/**
	 * Saves the board of the opposing player to reduce the information
	 * tranfered between players during the game
	 * 
	 * @param board from the opposing player
	 */
	public void copyBoard(Board other) {
		for(int i = 0; i < boardX; i++) {
			for(int j = 0; j < boardY; j++) {
				fieldStatus[i][j] = other.fieldStatus[i][j];
			}
		}

		fleet = new ArrayList<Ship>(other.fleet);
	}
	
	/**
	 * @returns the size of the board in the x-direction
	 * 
	 */
	public int getXdim()
	{
		return boardX;
	}
	
	/**
	 * @returns the size of the board in the y-direction
	 * 
	 */	
	public int getYdim()
	{
		return boardY;
	}
	
	/**
	 * @param x 	x-coordinate of the coordinate that is searched for
	 * @param y 	y-coordinate of the coordinate that is searched for 
	 * @returns the fieldstatus of the coordinate (x,y)
	 */	
	public int getFieldStatus(int x, int y)
	{
		return fieldStatus[x][y];
	}
	
	/**
	 * 
	 * @returns fleet, the list containing the ships
	 */
	public ArrayList<Ship> getFleet()
	{
		return fleet;
	}
}
