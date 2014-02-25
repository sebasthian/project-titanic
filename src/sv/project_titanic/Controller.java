package sv.project_titanic;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import sv.project_titanic.model.*;
import sv.project_titanic.connection.*;

/**The heart of the game, handles all logic.*/
public class Controller {
	private Board awayBoard;
	private	Board homeBoard;
	private boolean playerTurn;

	private Thread serverThread;
	private TCPServer server;
	private TCPClient client;

	/**Create and initialize a new controller. Creates a new TCPClient and adds
	 * a listener to it to handle messages from the opponent.
	 *
	 * @param awayBoard opponent's Board.
	 * @param homeBoard local player's Board.
	 */
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

	/**Host a new game on port 6665 and join it.
	 *
	 * @return true if the server was started successfully and the game could
	 *         be joined, fales otherwise.
	 */
	public boolean hostGame() {
		server = new TCPServer(6665);
		serverThread = new Thread(server);

		serverThread.start();

		playerTurn = true;

		return joinGame("127.0.0.1");
	}

	/**Join an existing game.
	 *
	 * @param ip the ip address of the game.
	 *
	 * @return true if the game could be joined.
	 */
	public boolean joinGame(String ip) {
		return client.connect(ip);
	}

	/**Start a game by sending the local Board to the opponent.*/
	public void startGame() {
		client.send(homeBoard);
	}

	/**Copy the Board sent by the opponent into awayBoard.
	 *
	 * @param board the opponents Board.
	 */
	public void initializeAwayBoard(Board board) {
		awayBoard.copyBoard(board);
	}

	/**Register a move sent by the opponent and allow the local player to make
	 * their next turn.
	 *
	 * @param move an array of the form {x, y, status}
	 */
	public void opponentsMove(int[] move) {
		homeBoard.setFieldStatus(move[0], move[1], move[2]);

		if(isGameOver())
			exitGame();

		playerTurn = true;
	}

	/**Let the local player shoot at the board. Change turns if the shot was
	 * successful. Used as a callback from the GUI.
	 *
	 * @param coord the Coordinate to shoot at.
	 */
	public void shoot(Coordinate coord) {
		if(playerTurn && placeShot(coord)) {
			playerTurn = false;

			if(isGameOver())
				exitGame();
		}
	}

	/**If possible, place a shot on the board, updating the status accordingly.
	 *
	 * @param coord the coord to place a shot at.
	 *
	 * @return true if a shot could be placed, false otherwise.
	 */
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

	/**Update the status of a coordinate and send the same info to the
	 * opponent.
	 *
	 * @param x x-coordinate of cell to update.
	 * @param y y-coordinate of cell to update.
	 * @param status the new status.
	 */
	public void updateStatus(int x, int y, int status) {
		int[] message = {x, y, status};
		client.send(message);
		awayBoard.setFieldStatus(x, y, status);
	}

	/**@return true if a player has lost all ships, false otherwise.*/
	public boolean isGameOver() {
		return awayBoard.allShipsSunk() || homeBoard.allShipsSunk();
	}

	public boolean replay(){
		return false;
	}

	/**Quit the game.*/
	public void exitGame(){
		System.exit(0);
	}
	
	/**Check if a Ship can be placed on the homeBoard without collision.
	 *
	 * @param ship the ship to place.
	 *
	 * @return true if the Ship can be placed, false otherwise.
	 */
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
	
	/**Place a Ship on the Boad, if possible.
	 *
	 * @param ship the Ship to place.
	 *
	 * @return true if the Ship could be placed, false otherwise.
	 */
	public boolean placeShip(Ship ship) {
		if(!canPlaceShip(ship))
			return false;

		for(Coordinate c : ship.getCoords())	
			homeBoard.setFieldStatus(c.getX(), c.getY(), 2);

		homeBoard.addShip(ship);

		return true;
	}
}

