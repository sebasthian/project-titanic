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
	
	public void addShip(Ship ship)
	{
		fleet.add(ship);
	}
	
	public void setFieldStatus(int x, int y, int status)
	{
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
	}

	public void copyBoard(Board other) {
		for(int i = 0; i < boardX; i++) {
			for(int j = 0; j < boardY; j++) {
				fieldStatus[i][j] = other.fieldStatus[i][j];
			}
		}

		fleet = new ArrayList<Ship>(other.fleet);
	}
	
	public int getXdim()
	{
		return boardX;
	}
	
	public int getYdim()
	{
		return boardY;
	}
	
	public int getFieldStatus(int x, int y)
	{
		return fieldStatus[x][y];
	}

	public ArrayList<Ship> getFleet()
	{
		return fleet;
	}
}
