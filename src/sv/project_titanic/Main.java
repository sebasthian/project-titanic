package sv.project_titanic;

import sv.project_titanic.*;
import sv.project_titanic.model.*;
import sv.project_titanic.view.*;

public class Main {
    public static void main(String[] args) {
        Board homeBoard = new Board();
        Board awayBoard = new Board();

        Player homePlayer = new Player();
        Player awayPlayer = new Player();

        //Controller controller = new Controller();

        GUI gui = new GUI(homeBoard, homePlayer, awayPlayer);
    }
}

