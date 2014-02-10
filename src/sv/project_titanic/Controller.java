package sv.project_titanic;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller {
  
  /**
	 * Class constructor. 
	 */
	public control(){
		
		
	}
	/**
	 * Translate the x-coordinate from event to column index.
	 * @param x 	the x-coordinate from event.
	 * @return 		the index of the actual column.
	 */
	public int mouseXtoCol(int x){  //Behöver fixas till, beror på hur vi dimensionerar board.
		int column = x; 
		
		if(x >= 0 && x < board.getXdim()){
			return column;
		}
		return -1;
	}
	/**
	 * Translate the y-coordinate from event to row index.
	 * @param y 	the y-coordinate from event.
	 * @return  	the index of the actual row.
	 */
	public int mouseYtoRow(int y){   //Behöver fixas till, beror på hur vi dimensionerar board.
		int row = y; 
		
		if(y >= 0 && y < board.getYdim()){
			return row;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param x 
	 * @param y 
	 */
	public boolean isClickable(int x, int y){   //Bättre om denna finns i GUI?
		return true;
	}
	
	/**
	 * Tries to shoot at a given coordinate.
	 * @param x  	x-coordinate from event.
	 * @param y 	y-coordinate from event.
	 */
	public void shoot(Coordinate c){
		
		int result = 0;
		
		if(canPlaceShot(c)){
			try {
				result = placeShot(c);
			}
			catch(InvalidShotException e){
				//Hur ska vi hantera den? Hur hanterar vi status i programmet?
			}
			if(result == 1){
				//MISS
			} else if(result == 3){
				//TRÄFF
			} else if(result == 4){
				//Sänkt skäpp
			}		
		} else {
			//Något gick fel.
		}
		if(isGameOver()){
			//Spelet är slut
		}
	}
	/**
	 * Reads the status of the coordinate.
	 * @param c   object of coordinate.
	 * @return 		true if coordinate not been shot before, otherwise false.
	 */
	public boolean canPlaceShoot(Coordinate c){
		int x = c.getX();		
		int y = c.getY();
		
		int status = board.getFieldStatus(x,y);
						
		if(status == 0 || status == 2){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Place a shot on the given coordinate. 
	 * @param c   object containing coordinate.
	 * @throws 		InvalidShotException is thrown if the field has already been shot.
	 */
	public void placeShot(Coordinate c){
		throws InvalidShotException{
			int x = c.getX();
			int y = c.getY();
			
			int status = board.getFieldStatus(x,y);
			
			if(status == 1 || status == 3){
				throw new InvalidShotException(c, "This field has allready been hit.");
			}else if(status == 0){
				board.setFieldStatus(x,y,status+1);
			}else{
				for(Ship shit : fleet){
					if(ship.hasCoordinates(c)){
						ship.shipHit(c);
					}
				}
				board.setFieldStatus(x,y,status+1);
			}
		}
	}
	/**
	 * Checks if fleet still contains coordinate. If it does, 
	 * all ships are not sunk and game continues.
	 * @return 		true if fleet is empty, otherwise false.
	 */
	public boolean isGameOver(){
		if(fleet.isEmpty()){
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Check if the ship can be placed on the board.
	 * @param s 	object of a ship.
	 * @return 		true if able to place ship, otherwise false.
	 */
	public boolean canPlaceShip(Ship s){
		
		Iterator<Coordinate> itr = s.coords.iterator();
		
		while(itr.hasNext()){
			Coordinate currentCoord = itr.next();
			
			int x = currentCoord.getX();
			int y = currentCoord.getY();
			
			//Om det redan finns en båt på denna koordinat.
			if(board.getFieldStatus(x,y) != 0){
				return false;
			} 

			// Är vi utanför spelbrädan?
			if(x >= board.getYdim() || y >= board.getXdim()){
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
		throws FieldOccupiedException {
			
			Iterator<Coordinate> itr = s.coords.iterator();
			
			while(itr.hasNext()){
				Coordinate newCoord = itr.next();
				
				int x = newCoord.getX();
				int y = newCoord.getY();
				
				int status = board.fieldStatus(x,y);
				
				if( status == 2){
					throw new FieldOccupiedException(newCoord, "Field occupied.");
				} else {
					board.setFieldStatus(x,y,status);
				}
				fleet.add(s);
			}
		}
	}
}

