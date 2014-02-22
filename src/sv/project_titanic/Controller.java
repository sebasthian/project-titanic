package sv.project_titanic;

import java.util.ArrayList;
import java.util.Iterator;

import sv.project_titanic.model.*;

public class Controller {
	private Board awayBoard;
	private	Board homeBoard;
	private boolean playerTurn;

	public Controller(Board awayBoard, Board homeBoard) {
		this.awayBoard = awayBoard;
		this.homeBoard = homeBoard;
		playerTurn = true;
	}

	public void shoot(Coordinate coord) {
		if(playerTurn && placeShot(coord)) {
			//playerTurn = false;

			if(isGameOver())
				exitGame();
		}
	}

	public boolean placeShot(Coordinate coord) {
		int x = coord.getX();
		int y = coord.getY();

		int status = awayBoard.getFieldStatus(x, y);

		switch(status) {
			case 0:
				awayBoard.setFieldStatus(x,y,1);
				return true;

			case 2:
				Ship ship = awayBoard.getShipByCoord(coord);

				ship.shipHit(coord);

				if(ship.noMoreShip()) {
					for(Coordinate c : ship.getCoords())
						awayBoard.setFieldStatus(c.getX(), c.getY(), 4);
				}
				else {
					awayBoard.setFieldStatus(x,y,3);
				}

				return true;
		}

		return false;
	}

	public boolean isGameOver(){
		for(Ship ship : awayBoard.getFleet()){
			if(!ship.noMoreShip()){
				return false;				
			}
		}
		return true;
	}

	public boolean replay(){
		return false;
	}

	public void exitGame(){
		System.exit(0);
	}
	
	public boolean canPlaceShip(Ship ship) {
		for(Coordinate c : ship.getCoords()) {
			int x = c.getX();
			int y = c.getY();

			if(x >= homeBoard.getYdim() || y >= homeBoard.getXdim()) {
				return false;
			}

			if(homeBoard.getFieldStatus(x,y) != 0) {
				return false;
			}
		}

		return true;
	}
	
	public boolean placeShip(Ship ship) {
		if(!canPlaceShip(ship))
			return false;

		for(Coordinate c : ship.getCoords()) {
			int x = c.getX();
			int y = c.getY();

			homeBoard.setFieldStatus(x,y,2);
			awayBoard.setFieldStatus(x,y,2);
		}
		awayBoard.addShip(ship);
		homeBoard.addShip(ship);

		return true;
	}
}

