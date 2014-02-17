package sv.project_titanic;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import sv.project_titanic.*;
import sv.project_titanic.model.*;
import sv.project_titanic.view.*;

public class Main {
    public static void main(String[] args) {
        Board homeBoard = new Board(10, 10);
        Board awayBoard = new Board(10, 10);

        Player homePlayer = new Player("player1");
        Player awayPlayer = new Player("player2");

        Controller controller = new Controller(awayBoard, homeBoard, true);

        GUI gui = new GUI(homeBoard, awayBoard, homePlayer, awayPlayer, controller);

        SwingUtilities.invokeLater(gui);
    }
}

