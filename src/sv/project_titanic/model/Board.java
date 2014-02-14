package sv.project_titanic.model;

import java.util.ArrayList;

import sv.project_titanic.model.Ship;

import java.util.Observable;

public class Board extends Observable
{
	private int boardX, boardY;
	private ArrayList<Ship> fleet;
	//private boolean playerTurn;
	public int[][] fieldStatus;
	private ArrayList<Coordinate> coordList;
	
	public Board(int xdim, int ydim, ArrayList<Ship> fleetArray)
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
		
		for(Ship ship : fleetArray){
			for(Coordinate c : ship.getCoords() ){
				setFieldStatus(c.getX(),c.getY(), 2);
			}
		}
		
		addFleet(fleetArray);
		//initializeFieldStatus(fleetArray);
		
	}
	
	/*private void initializeFieldStatus(ArrayList<Ship> ships)
	{
		//Coordinate c = new Coordinate(x,y);
		
		for(int i=0 ; i<boardX ; i++){
			for(int j=0 ; j<boardY; j++){
				coordList.add(c);
				
				for(Ship boat : ships)
				{
					if(boat.hasCoordinate())
					{
						fieldStatus[i][j] = 2;
					}
					else
						fieldStatus[i][j] = 0;
				}
			}
		}		
	}*/
	

	private void addFleet(ArrayList<Ship> ship)
	{
		for(Ship boat : ship)
		{
			fleet.add(boat);
		}
	}
	
	public void setFieldStatus(int x, int y, int status)
	{
		fieldStatus[x][y] = status;
		setChanged();
		int[] message = {x, y, status};
		notifyObservers(message);
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
	
	
}
