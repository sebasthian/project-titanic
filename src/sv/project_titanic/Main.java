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

        Controller controller = new Controller(awayBoard, homeBoard);

        GUI gui = new GUI(homeBoard, awayBoard, controller);

        SwingUtilities.invokeLater(gui);
    }
}

