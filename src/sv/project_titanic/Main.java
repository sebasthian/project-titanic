package sv.project_titanic;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import sv.project_titanic.*;
import sv.project_titanic.model.*;
import sv.project_titanic.view.*;

public class Main {
    public static void main(String[] args) {
    	ArrayList<Coordinate> coord = new ArrayList<Coordinate>();
    	ArrayList<Ship> ship = new ArrayList<Ship>();
    	
    	Coordinate coord1 = new Coordinate(1,1);
    	Coordinate coord2 = new Coordinate(1,2);
    	Coordinate coord3 = new Coordinate(1,3);
    	
    	coord.add(coord1);
    	coord.add(coord2);
    	coord.add(coord3);
    	
    	Ship ship1 = new Ship(coord);
    	ship.add(ship1);
    	
        Board homeBoard = new Board(10, 10, ship);
        Board awayBoard = new Board(10, 10, ship);

        Player homePlayer = new Player("player1");
        Player awayPlayer = new Player("player2");

        Controller controller = new Controller(awayBoard, homeBoard, true);

        GUI gui = new GUI(homeBoard, awayBoard, homePlayer, awayPlayer, controller);

        SwingUtilities.invokeLater(gui);
    }
}

