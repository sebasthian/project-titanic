package sv.project_titanic;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import sv.project_titanic.model.*;
import sv.project_titanic.connection.*;

public class Controller {
	private Board awayBoard;
	private	Board homeBoard;
	private boolean playerTurn;

	private Thread serverThread;
	private TCPServer server;
	private TCPClient client;

	public Controller(Board awayBoard, Board homeBoard) {
		this.awayBoard = awayBoard;
		this.homeBoard = homeBoard;
		playerTurn = false;

		client = new TCPClient();

		client.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object message = ((ClientActionEvent)e).getRecieveObject();

				if(message instanceof Board)
					initializeAwayBoard((Board)message);
				else if(message instanceof int[])
					opponentsMove((int[])message);
				else if(message instanceof String)
					System.out.println((String)message);
			}
		});
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

	public void startGame() {
		client.send(homeBoard);
	}

	public void initializeAwayBoard(Board board) {
		awayBoard.copyBoard(board);
	}

	public void opponentsMove(int[] move) {
		homeBoard.setFieldStatus(move[0], move[1], move[2]);

		if(isGameOver())
			exitGame();

		playerTurn = true;
	}

	public void shoot(Coordinate coord) {
		if(playerTurn && placeShot(coord)) {
			playerTurn = false;

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
				updateStatus(x, y, 1);
				return true;

			case 2:
				Ship ship = awayBoard.getShipByCoord(coord);

				ship.shipHit(coord);

				if(ship.noMoreShip()) {
					for(Coordinate c : ship.getCoords())
						updateStatus(c.getX(), c.getY(), 4);
				}
				else {
					updateStatus(x, y, 3);
				}

				return true;
		}

		return false;
	}

	public void updateStatus(int x, int y, int status) {
		int[] message = {x, y, status};
		client.send(message);
		awayBoard.setFieldStatus(x, y, status);
	}

	public boolean isGameOver() {
		for(Ship ship : awayBoard.getFleet()) {
			if(!ship.noMoreShip())
				return false;
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

			if(x >= homeBoard.getYdim() || y >= homeBoard.getXdim())
				return false;

			if(homeBoard.getFieldStatus(x, y) != 0)
				return false;
		}

		return true;
	}
	
	public boolean placeShip(Ship ship) {
		if(!canPlaceShip(ship))
			return false;

		for(Coordinate c : ship.getCoords())	
			homeBoard.setFieldStatus(c.getX(), c.getY(), 2);

		homeBoard.addShip(ship);

		return true;
	}
}

