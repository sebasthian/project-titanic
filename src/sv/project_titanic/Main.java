package sv.project_titanic;

import java.util.ArrayList;

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

        Player homePlayer = new Player();
        Player awayPlayer = new Player();

        //Controller controller = new Controller();

        GUI gui = new GUI(homeBoard, homePlayer, awayPlayer);
    }
}

