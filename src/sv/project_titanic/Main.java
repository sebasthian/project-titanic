package sv.project_titanic;

import sv.project_titanic.*;
import sv.project_titanic.model.*;
import sv.project_titanic.view.*;

public class Main {
    public static void main(String[] args) {
        Board homeBoard = new Board(10, 10, new Ship[0]);
        Board awayBoard = new Board(10, 10, new Ship[0]);

        Player homePlayer = new Player();
        Player awayPlayer = new Player();

        //Controller controller = new Controller();

        GUI gui = new GUI(homeBoard, homePlayer, awayPlayer);
    }
}

