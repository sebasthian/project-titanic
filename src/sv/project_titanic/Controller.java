package sv.project_titanic;

import sv.project_titanic.model.*;


import java.util.ArrayList;
import java.util.Iterator;



public class Controller {
  
	private Board currentBoard;
	private Board awayBoard;
	private	Board homeBoard;
	private boolean playerTurn;
	private Ship ship;
	private ArrayList<Coordinate> coord;
	
	
  /**
	 * Class constructor. 
	 */
	public Controller(Board ab, Board hb, boolean turn){
		awayBoard = ab;
		homeBoard = hb;
		playerTurn = turn;
	}
	/**
	 * Tries to shoot at a given coordinate.
	 * @param x  	x-coordinate from event.
	 * @param y 	y-coordinate from event.
	 */
	public void shoot(Coordinate c){
		
		//int result = 0;
		
		if(playerTurn){
			
			currentBoard = awayBoard;
			
			if(canPlaceShot(c)){
				placeShot(c);		
			}
		}
		if(isGameOver()){

		}
	}
	/**
	 * Reads the status of the coordinate.
	 * @param c   object of coordinate.
	 * @return 		true if coordinate not been shot before, otherwise false.
	 */
	public boolean canPlaceShot(Coordinate c){
		int x = c.getX();		
		int y = c.getY();
		
		int status = currentBoard.getFieldStatus(x,y);
						
		if(status == 0 || status == 2){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Place a shot on the given coordinate. 
	 * @param c   object containing coordinate.
	 * @return 
	 */
	public int placeShot(Coordinate c){
		
		int x = c.getX();
		int y = c.getY();
			
		int status = currentBoard.getFieldStatus(x,y);
			
		if(status == 1 || status == 3){
			//Redan beskjuten ruta.
			//skicka statusmeddelande till logg-f�nstret.
			return -1;
		}else if(status == 0){
			status++;
			currentBoard.setFieldStatus(x,y,status);
			
			return status;
		} else{
			for(Ship ship : currentBoard.getFleet()){
				if(ship.hasCoordinate(c)){
					ship.shipHit(c);
					break;
				}
			}
			board.setFieldStatus(x,y,status+1);
		}
		
		currentBoard.setFieldStatus(x,y,status+1);
			
		return status+1;
		
	}
	/**
	 * Checks if fleet still contains coordinate. If it does, 
	 * all ships are not sunk and game continues.
	 * @return 		true if fleet is empty, otherwise false.
	 */
	public boolean isGameOver(){
		for(Ship ship : currentBoard.getFleet()){
			if(!ship.noMoreShip()){
				return false;				
			}
		}
		return true;
	}
	/**
	 * Check if the ship can be placed on the board.
	 * @param s 	object of a ship.
	 * @return 		true if able to place ship, otherwise false.
	 */
	public boolean canPlaceShip(Ship s){
		
		Iterator<Coordinate> itr = coord.iterator();
		
		while(itr.hasNext()){
			Coordinate currentCoord = itr.next();
			
			int x = currentCoord.getX();
			int y = currentCoord.getY();
			
			//Om det redan finns en båt på denna koordinat.
			if(currentBoard.getFieldStatus(x,y) != 0){
				return false;
			} 

			// Är vi utanför spelbrädan?
			if(x >= currentBoard.getYdim() || y >= currentBoard.getXdim()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Places a ship on the grid.
	 * @param s 	object of a ship.
	 * @throws 		FieldOccupiedException if the field already 
	 * 				    has a ship placed on that coordinate.
	 */	
	public void placeShip(Ship s){
			Iterator<Coordinate> itr = coord.iterator();
			
			while(itr.hasNext()){
				Coordinate newCoord = itr.next();
				
				int x = newCoord.getX();
				int y = newCoord.getY();
				
				int status = currentBoard.getFieldStatus(x,y);
				
				if( status == 2){

				} else {
					currentBoard.setFieldStatus(x,y,status);
				}
				//ship.add(s);
			}
	}
}

