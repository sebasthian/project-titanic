package sv.project_titanic;

import sv.project_titanic.model.*;
import sv.project_titanic.connection.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;


public class Controller {
  
	private Board currentBoard;
	private Board awayBoard;
	private	Board homeBoard;
	private boolean playerTurn;
	private Ship ship;
	private ArrayList<Coordinate> coord;
	private Thread serverThread;
	private TCPServer server;
	private TCPClient client;
	
	
  /**
	 * Class constructor. 
	 */
	public Controller(Board ab, Board hb, boolean turn){
		awayBoard = ab;
		homeBoard = hb;
		playerTurn = false;

		client = new TCPClient();

		client.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientActionEvent c = (ClientActionEvent)e;

				Object message = c.getRecieveObject();

				if(message instanceof Board)
					initializeAwayBoard((Board)message);
				else if(message instanceof int[])
					opponentsMove((int[])message);
				else if(message instanceof String)
					System.out.println((String)message);
			}
		});
	}

	public void startGame() {
		client.send(homeBoard);
	}

	public void initializeAwayBoard(Board board) {
		awayBoard.copyBoard(board);
	}

	public void opponentsMove(int[] move) {
		homeBoard.setFieldStatus(move[0], move[1], move[2]);

		playerTurn = true;
	}

	public boolean hostGame() {
		server = new TCPServer(6665);
		serverThread = new Thread(server);
		
		serverThread.start();

		playerTurn = true;

		return joinGame("127.0.0.1");
	}

	public boolean joinGame(String ip) {
		return client.connect(ip);
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
				playerTurn = false;
			}
		}
		if(isGameOver()){
			System.out.println("GAME OVER");
			exitGame();
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
	public void placeShot(Coordinate c){
		
		int x = c.getX();
		int y = c.getY();
			
		int status = currentBoard.getFieldStatus(x,y);
			
		if(status == 1 || status == 3){
			//Redan beskjuten ruta.

		}else if(status == 0){

			currentBoard.setFieldStatus(x,y,1);
			int[] message = {x, y, 1};
			client.send(message);

		} else{
			for(Ship ship : currentBoard.getFleet()){
				if(ship.hasCoordinate(c)){
					ship.shipHit(c);
					
					if(ship.noMoreShip()){
						for(Coordinate cc : ship.getCoords()){
							currentBoard.setFieldStatus(cc.getX(), cc.getY(), 4);
							int[] message = {cc.getX(), cc.getY(), 4};
							client.send(message);
						}
					}else{
						currentBoard.setFieldStatus(x,y,3);
						int[] message = {x, y, 3};
						client.send(message);
					}
					break;
				}
			}
		}
		
		//currentBoard.setFieldStatus(x,y,status+1);

		
	}
	/**
	 * Checks if fleet still contains coordinate. If it does, 
	 * all ships are not sunk and game continues.
	 * @return 		true if fleet is empty, otherwise false.
	 */
	public boolean isGameOver(){
		for(Ship ship : awayBoard.getFleet()){
			if(!ship.noMoreShip()){
				return false;				
			}
		}
		return true;
	}
	/**
	 * Checks if the players wants to play another run.
	 * @return		true if another run shall me played.
	 */
	public boolean replay(){
		return false;
	}
	/**
	 * Kills the game.
	 */
	public void exitGame(){
		System.exit(0);
	}
	
	/**
	 * Check if the ship can be placed on the board.
	 * @param s 	object of a ship.
	 * @return 		true if able to place ship, otherwise false.
	 */
	public boolean canPlaceShip(Ship s){
		Iterator<Coordinate> itr = s.getCoords().iterator();
		
		while(itr.hasNext()){
			Coordinate currentCoord = itr.next();

			int x = currentCoord.getX();
			int y = currentCoord.getY();

			// Är vi utanför spelbrädan?
			if(x >= homeBoard.getYdim() || y >= homeBoard.getXdim()){
				return false;
			}

			//Om det redan finns en båt på denna koordinat.
			if(homeBoard.getFieldStatus(x,y) != 0){
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
		Iterator<Coordinate> itr = s.getCoords().iterator();

		while(itr.hasNext()){
			Coordinate newCoord = itr.next();

			int x = newCoord.getX();
			int y = newCoord.getY();

			int status = homeBoard.getFieldStatus(x,y);

			if( status == 2){

			} else {
				homeBoard.setFieldStatus(x,y,2);
			}
			
		}
		homeBoard.addShip(s);
	}
}

