package sv.project_titanic.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.border.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI extends JFrame {
    private final static Color SHIP_COLOR = Color.BLACK;
    private final static Color EMPTY_COLOR = Color.BLUE;
    private final static Color MISS_COLOR = new Color(100, 100, 255);
    private final static Color HIT_COLOR = Color.RED;
    private final static Color SUNK_COLOR = Color.GREEN;

    private Grid homeGrid;
    private Grid awayGrid;
    private JLabel homePlayer;
    private JLabel awayPlayer;
    private JLabel turnMessage;

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        homeGrid = new Grid(10, 10, false);
        awayGrid = new Grid(10, 10, true);

        homeGrid.getCell(5, 5).setColor(SHIP_COLOR);
        homeGrid.getCell(5, 6).setColor(SHIP_COLOR);
        homeGrid.getCell(5, 7).setColor(SHIP_COLOR);

        homeGrid.getCell(2, 1).setColor(HIT_COLOR);
        homeGrid.getCell(3, 1).setColor(SHIP_COLOR);

        homeGrid.getCell(9, 3).setColor(SUNK_COLOR);
        homeGrid.getCell(9, 4).setColor(SUNK_COLOR);
        homeGrid.getCell(9, 5).setColor(SUNK_COLOR);
        homeGrid.getCell(9, 6).setColor(SUNK_COLOR);
                             
        homeGrid.getCell(1, 1).setColor(MISS_COLOR);
        homeGrid.getCell(3, 5).setColor(MISS_COLOR);
        homeGrid.getCell(9, 9).setColor(MISS_COLOR);
        homeGrid.getCell(6, 6).setColor(MISS_COLOR);
        homeGrid.getCell(3, 9).setColor(MISS_COLOR);

        homePlayer = new JLabel("player 1");
        awayPlayer = new JLabel("player 2");
        turnMessage = new JLabel("Your turn");

        buildFrame();
    }

    private void buildFrame() {
        setTitle("Project Titanic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel statusArea = new JPanel(new BorderLayout());
        statusArea.add(homePlayer, BorderLayout.WEST);
        statusArea.add(awayPlayer, BorderLayout.EAST);

        turnMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        turnMessage.setHorizontalAlignment(SwingConstants.CENTER);
        statusArea.add(turnMessage, BorderLayout.CENTER);

        add(statusArea, BorderLayout.NORTH);

        add(new JLabel("Maybe an exit button down here."), BorderLayout.SOUTH);

        JPanel gameArea = new JPanel(new GridLayout(1, 2, 6, 0));
        gameArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        gameArea.add(homeGrid);
        gameArea.add(awayGrid);

        add(gameArea, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

