package sv.project_titanic.model;

import java.util.ArrayList;


public class Board extends Observable
{
	private int boardX, boardY;
	private ArrayList<Ship> fleet;
	private boolean playerTurn;
	private int[][] fieldStatus;
	private Coordinate[][] coordList;
	
	public Board(int x, int y, Ship[] fleetArray)
	{
		this.boardX = x;
		this.boardY = y;
		addFleet(fleetArray);
		initializeFieldStatus(fleet);
	}
	
	private void initializeFieldStatus(ArrayList<Ship> ships)
	{
		for(i=0 ; i<boardX ; i++)
		{
			for(j=0 ; j<boardY; j++)
			{
				coordList[i][j].add(new Coordinate(i, j));
				
				for(Ship boat : ships)
				{
					if(boat.hasCoordinate(coordList[i][j]))
					{
						fieldStatus[i][j]=2;
					}
					else
						fieldStatus[i][j] = 0;
				}
			}
		}		
	}
	
	private void addFleet(Ship[] ships)
	{
		for(Ship boat : ships)
		{
			fleet.add(boat);
		}
	}
	
	public void setFieldStatus(int x, int y, int status)
	{
		fieldStatus[x][y] = status;
		hasChanged();
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
